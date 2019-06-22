package com.software.eric.coolweather.mvc.key

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.software.eric.coolweather.R
import kotlinx.android.synthetic.main.activity_key_setting.*

class KeySettingActivity : AppCompatActivity(), KeyContract.View {

    companion object {

        @JvmStatic
        fun launch(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, KeySettingActivity::class.java), requestCode)
        }
    }

    private val keySettingPresenter = KeySettingPresenter(this, KeySettingRepo())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_setting)
        submitBtn.setOnClickListener {
            keySettingPresenter.onSubmit(keyInput.text.toString())
        }
    }

    override fun onInputEmpty() {
        Toast.makeText(this, getString(R.string.hint_input_key), Toast.LENGTH_SHORT).show()
    }

    override fun onKeySaved() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
