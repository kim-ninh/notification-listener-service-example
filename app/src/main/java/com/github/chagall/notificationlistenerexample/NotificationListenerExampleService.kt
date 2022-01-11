package com.github.chagall.notificationlistenerexample

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

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
class NotificationListenerExampleService : NotificationListenerService() {
    companion object {
        private val _notiAction = MutableLiveData<String>()
        val notiAction: LiveData<String>
            get() = _notiAction

        private val _statusBarNoti = MutableLiveData<StatusBarNotification>()
        val statusBarNoti: LiveData<StatusBarNotification>
            get() = _statusBarNoti
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        _statusBarNoti.postValue(sbn)
        _notiAction.postValue("posted")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        _statusBarNoti.postValue(sbn)
        _notiAction.postValue("removed")
    }
}
