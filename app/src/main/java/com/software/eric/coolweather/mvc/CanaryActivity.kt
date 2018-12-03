package com.software.eric.coolweather.mvc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import com.software.eric.coolweather.R
import com.software.eric.coolweather.util.LogUtil
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
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
