package com.software.eric.coolweather.mvc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.bq.openglcamera.Main2Activity
import com.bq.openglcamera.MainActivity
import com.software.eric.coolweather.R
import com.software.eric.coolweather.util.LogUtil
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_canary.*
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class CanaryActivity : AppCompatActivity(), View.OnClickListener {
    var finished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canary)
        ButterKnife.bind(this)
        glCamera.setOnClickListener(this)
        Camera2.setOnClickListener(this)
    }

    @OnClick(R.id.btn_block_queue)
    fun blockQueue() {
        val queue = LinkedBlockingQueue<Runnable>(5)
        val e = ThreadPoolExecutor(1, 1, 0L,
            TimeUnit.MILLISECONDS, queue)
        e.prestartCoreThread()
        Completable.fromRunnable {
            for (i in 1..40) {
                if (finished) {
                    queue.clear()
                    break
                }
                LogUtil.d("Eric", "$i task enqueue")
                queue.put(Runnable {
                    LogUtil.d("Eric", "$i task started")
                    Thread.sleep(1000)
                    LogUtil.d("Eric", "$i task completed")
                })
                LogUtil.d("Eric", "$i task enqueued")
            }
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    @OnClick(R.id.btn_write)
    fun onWrite() {
        val tmp = File("/sdcard")
        var writable = tmp.canWrite()
        LogUtil.d("Eric", "sdcard writable $writable")
        val dir = File(Environment.getExternalStorageDirectory(), "test")
        writable = dir.canWrite()
        LogUtil.d("Eric", "env writable $writable")
        if (!dir.exists()) {
            val b = dir.mkdirs()
            LogUtil.d("Eric", "mkdir result $b")
        } else {
            LogUtil.d("Eric", "mkdir exist")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finished = true
    }

    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.glCamera -> {
                MainActivity.start(this)
            }
            R.id.Camera2 -> {
                Main2Activity.start(this)
            }
            else -> {
            }
        }
    }

    companion object {
        @JvmStatic
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, CanaryActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
