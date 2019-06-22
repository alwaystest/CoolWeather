package com.software.eric.coolweather.mvc.key

/**
 * Created by eric on 2019-06-22
 * @author eric
 * @date yyyy/mm/dd 2019/06/22
 */
interface KeyContract {

    interface View {

        fun onInputEmpty()

        fun onKeySaved()

    }

    interface Repo {

        fun saveApiKey(key: String)

        fun getApiKey(): String
    }
}