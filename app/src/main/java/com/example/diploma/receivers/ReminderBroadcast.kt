package com.example.diploma.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.diploma.data.Events
import com.example.diploma.notifications.NotificationConstants
import com.example.diploma.notifications.NotificationHelper

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.getBundleExtra(NotificationConstants.EVENT_BUNDLE_KEY)
        val event: Events = bundle?.get(NotificationConstants.EVENT_KEY) as Events
        NotificationHelper.createNotification(context!!, event)
    }
}