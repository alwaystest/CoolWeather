package com.software.eric.coolweather.v2

import android.os.Bundle
import com.software.eric.coolweather.R
import com.software.eric.coolweather.base.BaseActivity
import com.software.eric.coolweather.v2.ui.main.MainFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}
