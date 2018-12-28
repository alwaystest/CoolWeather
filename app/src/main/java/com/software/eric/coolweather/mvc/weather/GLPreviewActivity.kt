package com.software.eric.coolweather.mvc.weather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.software.eric.coolweather.R
import kotlinx.android.synthetic.main.activity_glpreview.*
import android.graphics.SurfaceTexture
import android.view.TextureView



class GLPreviewActivity : AppCompatActivity() {

    private lateinit var mRenderThread: GLCameraRenderThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glpreview)
        textureView.surfaceTextureListener = TextureCallback()
    }

    internal inner class TextureCallback(private val mFilter: Int) : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture,
                                               width: Int, height: Int) {
            mRenderThread = GLCameraRenderThread(surface, mFilter)
            mRenderThread.setRegion(width, height)
            mRenderThread.start()
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            mRenderThread.setRegion(width, height)
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

        }

    }
}
