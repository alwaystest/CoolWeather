package com.software.eric.coolweather.mvc.weather

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.*
import com.software.eric.coolweather.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGL10

class GLCameraRenderThread(private val mSurface: SurfaceTexture, private val mFilter: Int) : Thread() {

    /*Draw region*/
    internal var mWidth: Int = 0
    internal var mHeight: Int = 0

    //XXX To fix a strange bug, which I cannot find the root cause.
    //When zoom out/in the filter, there's always an unpleasant flash after zoom
    //So I defer the size change for one frame.
    internal var mDeferWidth: Int = 0
    internal var mDeferHeight: Int = 0

    private var mProgram: Int = 0
    private var mTexName = 0

    private var mSuspend = false

    /*For EGL Setup*/
    private var mEgl: EGL10? = null
    private var mEglDisplay: EGLDisplay? = null
    private var mEglConfig: EGLConfig? = null
    private var mEglContext: EGLContext? = null
    private var mEglSurface: EGLSurface? = null

    /*Vertex buffers*/
    private var mVertexBuffer: FloatBuffer? = null
    private var mTexCoordBuffer: FloatBuffer? = null
    private var mDrawListBuffer: ShortBuffer? = null


    fun suspendRendering() {
        mSuspend = true
    }

    fun resumeRendering() {
        mSuspend = false
    }

    private fun compileShader(type: Int): Int {
        val program: Int
        val app = GLPreviewActivity.getAppInstance()

        val vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        val fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)

        val vertexShaderCode = readRawTextFile(app, R.raw.vertex)
        val fragmentShaderCode: String

        when (mFilter) {
            FILTER_GREY -> fragmentShaderCode = readRawTextFile(app, R.raw.fragment_grey_scale)
            FILTER_SEPIA_TONE -> fragmentShaderCode = readRawTextFile(app, R.raw.fragment_sepia_tone)
            FILTER_NEGATIVE_COLOR -> fragmentShaderCode = readRawTextFile(app, R.raw.fragment_negative_color)
            FILTER_FISHEYE -> fragmentShaderCode = readRawTextFile(app, R.raw.fragment_fish_eye)
            FILTER_CYAN -> fragmentShaderCode = readRawTextFile(app, R.raw.fragment_cyan)
            FILTER_RADIAL_BLUR -> fragmentShaderCode = readRawTextFile(app, R.raw.fragment_radial_blur)
            FILTER_H_MIRROR -> fragmentShaderCode = readRawTextFile(app, R.raw.fragment_h_mirror)
            FILTER_V_MIRROR -> fragmentShaderCode = readRawTextFile(app, R.raw.fragment_v_mirror)
            else -> fragmentShaderCode = readRawTextFile(app, R.raw.fragment_no_effect)
        }

        GLES20.glShaderSource(vertexShader, vertexShaderCode)
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode)

        val compileStatus = IntArray(1)
        GLES20.glCompileShader(vertexShader)
        GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] == 0) {
            val err = GLES20.glGetShaderInfoLog(vertexShader)
            throw RuntimeException("vertex shader compile failed:$err")
        }
        GLES20.glCompileShader(fragmentShader)
        GLES20.glGetShaderiv(fragmentShader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] == 0) {
            val err = GLES20.glGetShaderInfoLog(fragmentShader)
            throw RuntimeException("fragment shader compile failed:$err")
        }

        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)

        return program
    }

    fun prepareBuffer() {
        /*Vertex buffer*/
        val bb = ByteBuffer.allocateDirect(4 * shapeCoords.size)
        bb.order(ByteOrder.nativeOrder())

        mVertexBuffer = bb.asFloatBuffer()
        if (FILTER_FISHEYE == mFilter) {
            mVertexBuffer!!.put(shapeCoordsFishEye)
        } else {
            mVertexBuffer!!.put(shapeCoords)
        }
        mVertexBuffer!!.position(0)

        /*Vertex texture coord buffer*/
        val txeb = ByteBuffer.allocateDirect(4 * textureCoords.size)
        txeb.order(ByteOrder.nativeOrder())

        mTexCoordBuffer = txeb.asFloatBuffer()
        mTexCoordBuffer!!.put(textureCoords)
        mTexCoordBuffer!!.position(0)

        /*Draw list buffer*/
        val dlb = ByteBuffer.allocateDirect(drawOrder.size * 2)
        dlb.order(ByteOrder.nativeOrder())

        mDrawListBuffer = dlb.asShortBuffer()
        mDrawListBuffer!!.put(drawOrder)
        mDrawListBuffer!!.position(0)
    }

    private fun startPreview() {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)

        val app = GLPreviewActivity.getAppInstance()

        app.startCamera(textures[0])
        mTexName = textures[0]
    }

    private fun updatePreview() {
        val app = GLPreviewActivity.getAppInstance()
        app.updateCamPreview()
    }

    fun drawFrame() {
        GLES20.glUseProgram(mProgram)

        val positionHandler = GLES20.glGetAttribLocation(mProgram, "aPosition")
        val texCoordHandler = GLES20.glGetAttribLocation(mProgram, "aTextureCoord")
        val textureHandler = GLES20.glGetUniformLocation(mProgram, "sTexture")

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTexName)

        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        GLES20.glEnableVertexAttribArray(positionHandler)

        GLES20.glVertexAttribPointer(positionHandler, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            COORDS_PER_VERTEX * 4, mVertexBuffer)

        GLES20.glEnableVertexAttribArray(texCoordHandler)
        GLES20.glVertexAttribPointer(texCoordHandler, TEXTURE_COORS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            TEXTURE_COORS_PER_VERTEX * 4, mTexCoordBuffer)

        GLES20.glUniform1i(textureHandler, 0)

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.size, GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer)

        GLES20.glDisableVertexAttribArray(positionHandler)
        GLES20.glDisableVertexAttribArray(texCoordHandler)
    }

    private fun initGL() {
        /*Get EGL handle*/
        mEgl = EGLContext.getEGL() as EGL10

        /*Get EGL display*/
        mEglDisplay = mEgl!!.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)

        if (EGL10.EGL_NO_DISPLAY === mEglDisplay) {
            throw RuntimeException("eglGetDisplay failed:" + GLUtils.getEGLErrorString(mEgl!!.eglGetError()))
        }

        /*Initialize & Version*/
        val versions = IntArray(2)
        if (!mEgl!!.eglInitialize(mEglDisplay, versions)) {
            throw RuntimeException("eglInitialize failed:" + GLUtils.getEGLErrorString(mEgl!!.eglGetError()))
        }

        /*Configuration*/
        val configsCount = IntArray(1)
        val configs = arrayOfNulls<EGLConfig>(1)
        val configSpec = intArrayOf(EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT, EGL10.EGL_RED_SIZE, 8, EGL10.EGL_GREEN_SIZE, 8, EGL10.EGL_BLUE_SIZE, 8, EGL10.EGL_ALPHA_SIZE, 8, EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_STENCIL_SIZE, 0, EGL10.EGL_NONE)

        mEgl!!.eglChooseConfig(mEglDisplay, configSpec, configs, 1, configsCount)
        if (configsCount[0] <= 0) {
            throw RuntimeException("eglChooseConfig failed:" + GLUtils.getEGLErrorString(mEgl!!.eglGetError()))
        }
        mEglConfig = configs[0]

        /*Create Context*/
        val contextSpec = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
        mEglContext = mEgl!!.eglCreateContext(mEglDisplay, mEglConfig, EGL10.EGL_NO_CONTEXT, contextSpec)

        if (EGL10.EGL_NO_CONTEXT === mEglContext) {
            throw RuntimeException("eglCreateContext failed: " + GLUtils.getEGLErrorString(mEgl!!.eglGetError()))
        }

        /*Create window surface*/
        mEglSurface = mEgl!!.eglCreateWindowSurface(mEglDisplay, mEglConfig, mSurface, null)

        if (null == mEglSurface || EGL10.EGL_NO_SURFACE === mEglSurface) {
            throw RuntimeException("eglCreateWindowSurface failed" + GLUtils.getEGLErrorString(mEgl!!.eglGetError()))
        }

        /*Make current*/
        if (!mEgl!!.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
            throw RuntimeException("eglMakeCurrent failed:" + GLUtils.getEGLErrorString(mEgl!!.eglGetError()))
        }
    }

    @Synchronized
    override fun run() {
        val app = GLPreviewActivity.getAppInstance()

        initGL()

        mProgram = compileShader(FILTER_NONE)
        prepareBuffer()

        startPreview()

        while (true) {

            if (false == mSuspend) {
                synchronized(app) {
                    //
                    app.attachCameraTexture(mTexName)
                    app.updateCamPreview()
                    GLES20.glViewport(0, 0, mWidth, mHeight)
                    if (mDeferWidth != 0) {
                        mWidth = mDeferWidth
                        mHeight = mDeferHeight
                        mDeferWidth = 0
                    }
                    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
                    drawFrame()
                    app.detachCAmeraTexture()
                }

                if (!mEgl!!.eglSwapBuffers(mEglDisplay, mEglSurface)) {
                    throw RuntimeException("Cannot swap buffers")
                }
            }

            try {
                wait()
            } catch (e: Exception) {
                break
            }

        }
    }

    @Synchronized
    fun setRegion(width: Int, height: Int) {
        if (mWidth != 0) {
            mDeferWidth = width
            mDeferHeight = height
        } else {
            mWidth = width
            mHeight = height
            mDeferWidth = 0
        }
    }

    companion object {

        /*Filter Type Const*/
        val FILTER_NONE = 0
        val FILTER_GREY = 1
        val FILTER_SEPIA_TONE = 2
        val FILTER_NEGATIVE_COLOR = 3
        val FILTER_VIGNETTE = 4
        val FILTER_FISHEYE = 5
        val FILTER_CYAN = 6
        val FILTER_RADIAL_BLUR = 7
        val FILTER_H_MIRROR = 8
        val FILTER_V_MIRROR = 9

        private val shapeCoords = floatArrayOf(-1.0f, 1.0f, 0.0f, // top left
            -1.0f, -1.0f, 0.0f, // bottom left
            1.0f, -1.0f, 0.0f, // bottom right
            1.0f, 1.0f, 0.0f) // top right

        private val shapeCoordsFishEye = floatArrayOf(-1.0f, 0.5625f, 0.0f, // top left
            -1.0f, -0.5625f, 0.0f, // bottom left
            1.0f, -0.5625f, 0.0f, // bottom right
            1.0f, 0.5625f, 0.0f) // top right


        //90 degree rotated
        private val textureCoords = floatArrayOf(0.0f, 1.0f, // top left
            1.0f, 1.0f, // bottom left
            1.0f, 0.0f, // bottom right
            0.0f, 0.0f) // top right

        private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

        private val COORDS_PER_VERTEX = 3
        private val TEXTURE_COORS_PER_VERTEX = 2

        private fun readRawTextFile(context: Context, resId: Int): String {
            val inputStream = context.resources.openRawResource(resId)

            val inputreader = InputStreamReader(inputStream)
            val buffreader = BufferedReader(inputreader)
            var line: String
            val text = StringBuilder()

            try {
                while ((line = buffreader.readLine()) != null) {
                    text.append(line)
                    text.append('\n')
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return text.toString()
        }
    }
}
