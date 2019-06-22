package com.software.eric.coolweather.mvc.key

import android.preference.PreferenceManager
import com.software.eric.coolweather.util.MyApplication

/**
 * Created by eric on 2019-06-22
 * @author eric
 * @date yyyy/mm/dd 2019/06/22
 */
class KeySettingRepo : KeyContract.Repo {

    companion object {

        private const val KEY_API_KEY = "api_key"
    }

    private val sp by lazy {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext())
    }

    override fun saveApiKey(key: String) {
        sp.edit().putString(KEY_API_KEY, key).apply()
    }

    override fun getApiKey(): String {
        return sp.getString(KEY_API_KEY, "") ?: ""
    }
}