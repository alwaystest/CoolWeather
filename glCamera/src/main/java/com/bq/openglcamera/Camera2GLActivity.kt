package com.bq.openglcamera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.Surface
import android.view.TextureView
import com.bq.openglcamera.opengl.DefaultCameraRenderer
import com.bq.openglcamera.opengl.OnImageAvailableListener
import com.bq.openglcamera.opengl.TextureViewGLWrapper
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class Camera2GLActivity : AppCompatActivity() {

    companion object {

        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, Camera2GLActivity::class.java))
        }
    }

    var canOpenCamera = false
    var cameraManager: CameraManager? = null
    var cameraDevice: CameraDevice? = null
    var session: CameraCaptureSession? = null
    var surface: Surface? = null

    lateinit var textureView: TextureView
    var surfaceTexture: SurfaceTexture? = null

    val backgroundThread = HandlerThread("bg")
    lateinit var backgroundHandler: Handler

    lateinit var textureViewGLWrapper: TextureViewGLWrapper

    private lateinit var imageReader: ImageReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textureView = findViewById(R.id.texture_view) as TextureView

        val defaultCameraRenderer = DefaultCameraRenderer(this)

        textureViewGLWrapper = TextureViewGLWrapper(defaultCameraRenderer)
        textureViewGLWrapper.setListener({ texture ->
            surfaceTexture = texture
            openCamera()
        }, Handler(Looper.getMainLooper()))

        backgroundThread.start()
        backgroundHandler = Handler(backgroundThread.looper)

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                textureViewGLWrapper.onSurfaceTextureAvailable(surface, width, height)
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                textureViewGLWrapper.onSurfaceTextureSizeChanged(surface, width, height)
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                textureViewGLWrapper.onSurfaceTextureUpdated(surface)
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                textureViewGLWrapper.onSurfaceTextureDestroyed(surface)
                return true
            }
        }

        RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .subscribe { granted ->
                    canOpenCamera = granted
                    if (!granted) {
                        finish()
                    } else {
                        openCamera()
                    }
                }
    }

    override fun onResume() {
        super.onResume()
        openCamera()
    }

    override fun onPause() {
        super.onPause()
        closeCamera()
    }

    private val onImageAvailableListener: ImageReader.OnImageAvailableListener by lazy {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        OnImageAvailableListener(displayMetrics) {
            imageView.post{
                imageView.setImageBitmap(it)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        if (!canOpenCamera) return
        if (!textureView.isAvailable) return
        if (surfaceTexture == null) return
        if (cameraDevice != null) return

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraManager!!.openCamera("0", object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                this@Camera2GLActivity.cameraDevice = cameraDevice
                this@Camera2GLActivity.surface = Surface(surfaceTexture)
                val w = 1440
                val h = 1080
                surfaceTexture?.setDefaultBufferSize(w, h)
                val req = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                imageReader = ImageReader.newInstance(w, h, ImageFormat.YUV_420_888, 2).apply {
                    setOnImageAvailableListener(onImageAvailableListener, backgroundHandler)
                }
                req.addTarget(surface)
                req.addTarget(imageReader.surface)

                camera.createCaptureSession(listOf(surface, imageReader.surface), object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        req.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                        req.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON)
                        req.set(CaptureRequest.CONTROL_AE_ANTIBANDING_MODE, CaptureRequest.CONTROL_AE_ANTIBANDING_MODE_AUTO)
                        session.setRepeatingRequest(req.build(), null, null)
                        this@Camera2GLActivity.session = session
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession?) {
                        error("onConfigure Failed")
                    }
                }, null)
            }

            override fun onDisconnected(camera: CameraDevice?) {
            }

            override fun onError(camera: CameraDevice?, error: Int) {
                error("camera open failed")
            }
        }, null)
    }

    private fun closeCamera() {
        session?.close()
        session = null
        cameraDevice?.close()
        imageReader.close()
        surface?.release()
        cameraDevice = null
        surfaceTexture = null
    }
}