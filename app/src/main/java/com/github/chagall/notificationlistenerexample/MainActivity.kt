package com.github.chagall.notificationlistenerexample

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.github.chagall.notificationlistenerexample.databinding.ActivityMainBinding

/**
 * MIT License
 *
 * Copyright (c) 2016 Fábio Alves Martins Pereira (Chagall)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // If the user did not turn the notification listener service on we prompt him to do so
        if (!isNotificationServiceEnabled) {
            val enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog.show()
        }

        NotificationListenerExampleService.notiAction.observe(this) { notiAction ->
            binding.notiActionTextView.text = getString(R.string.noti_action, notiAction)
        }

        NotificationListenerExampleService.statusBarNoti.observe(this) { statusBarNotification ->
            val notification = statusBarNotification.notification
            val contentTitle = NotificationCompat.getContentTitle(notification)
            binding.notiPackageNameTextView.text =
                getString(R.string.package_name_template, statusBarNotification.packageName)
            binding.notiContentTitleTextView.text =
                getString(R.string.noti_title_template, contentTitle)
        }
    }

    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     * @return True if enabled, false otherwise.
     */
    private val isNotificationServiceEnabled: Boolean
        private get() {
            val pkgName = packageName
            val flat = Settings.Secure.getString(
                contentResolver,
                ENABLED_NOTIFICATION_LISTENERS
            )
            if (!TextUtils.isEmpty(flat)) {
                val names = flat.split(":").toTypedArray()
                for (i in names.indices) {
                    val cn = ComponentName.unflattenFromString(names[i])
                    if (cn != null) {
                        if (TextUtils.equals(pkgName, cn.packageName)) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    /**
     * Image Change Broadcast Receiver.
     * We use this Broadcast Receiver to notify the Main Activity when
     * a new notification has arrived, so it can properly change the
     * notification image
     */
    inner class ImageChangeBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            val receivedNotificationCode = intent.getIntExtra("Notification Code", -1)
//            changeInterceptedNotificationImage(receivedNotificationCode)
        }
    }

    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.notification_listener_service)
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation)
        alertDialogBuilder.setPositiveButton(
            R.string.yes
        ) { dialog, id -> startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        alertDialogBuilder.setNegativeButton(
            R.string.no
        ) { dialog, id ->
            // If you choose to not enable the notification listener
            // the app. will not work as expected
        }
        return alertDialogBuilder.create()
    }

    companion object {
        private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS =
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    }
}
