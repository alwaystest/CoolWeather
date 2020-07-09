package com.software.eric.coolweather.notification

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.software.eric.coolweather.R
import com.software.eric.coolweather.util.FileLogger

class NotiListenerService : NotificationListenerService() {

    companion object {

        private const val TAG = "NotiListenerService"

        private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"

        fun start(context: Context) {
            if (isNotificationListenGranted(context)) {
                requestRebind(ComponentName(context.applicationContext, NotiListenerService::class.java))
                Toast.makeText(context, R.string.notification_service_started, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, R.string.notification_permission_denied, Toast.LENGTH_SHORT).show()
                gotoNotificationListenerSetting(context)
            }
        }

        fun gotoNotificationListenerSetting(context: Context) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.applicationContext.startActivity(intent)
            }
        }


        fun isNotificationListenGranted(context: Context): Boolean {
            val packageName: String = context.applicationContext.packageName
            val flat = Settings.Secure.getString(
                    context.applicationContext.contentResolver, ENABLED_NOTIFICATION_LISTENERS)
            if (!TextUtils.isEmpty(flat)) {
                val names = flat.split(":")
                for (name in names) {
                    val cn = ComponentName.unflattenFromString(name)
                    if (cn != null && TextUtils.equals(packageName, cn.packageName)) {
                        return true
                    }
                }
            }
            return false
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d(TAG, "${sbn.notification}")
        FileLogger.log(TAG, sbn.notification.toString())
        FileLogger.log(TAG, sbn.notification.extras.getString(Notification.EXTRA_TITLE) ?: "empty title")
        FileLogger.log(TAG, sbn.notification.extras.getString(Notification.EXTRA_TEXT) ?: "empty text")
    }
}
