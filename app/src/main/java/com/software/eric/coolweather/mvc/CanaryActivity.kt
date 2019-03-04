package com.software.eric.coolweather.mvc

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.software.eric.coolweather.R
import com.software.eric.coolweather.mvc.weather.WeatherActivity
import com.software.eric.coolweather.util.LogUtil
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class CanaryActivity : AppCompatActivity() {
    var finished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canary)
        ButterKnife.bind(this)
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

    @OnClick(R.id.btn_notch_test)
    fun showDialog() {
        val v = View(this)
        v.setBackgroundColor(Color.RED)
        val lp = WindowManager.LayoutParams()
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        lp.gravity = Gravity.TOP or Gravity.CLIP_VERTICAL
        lp.height = 10
        lp.width = 20
        lp.y = 0
        lp.x = -200
        getWindowManager().addView(v, lp)
        val dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        val lp1 = WindowManager.LayoutParams()
        lp1.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        lp1.gravity = Gravity.TOP or Gravity.CLIP_VERTICAL
        lp1.height = 10
        lp1.width = 20
        lp1.y = 110
        lp1.x = 0
        dialog.window?.attributes = lp1
        val v1 = View(this)
        v1.setBackgroundColor(Color.GREEN)
        dialog.setContentView(v1)
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        finished = true
    }

    companion object {
        @JvmStatic
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, CanaryActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
