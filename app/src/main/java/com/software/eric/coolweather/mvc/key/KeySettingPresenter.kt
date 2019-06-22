package com.software.eric.coolweather.mvc.key

/**
 * Created by eric on 2019-06-22
 * @author eric
 * @date yyyy/mm/dd 2019/06/22
 */
class KeySettingPresenter(
        private val mView: KeyContract.View,
        private val mRepo: KeyContract.Repo
) {

    fun onSubmit(key: String) {
        if (key.isBlank()) {
            mView.onInputEmpty()
        } else {
            mRepo.saveApiKey(key)
            mView.onKeySaved()
        }
    }
}