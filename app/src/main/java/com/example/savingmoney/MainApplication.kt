package com.example.savingmoney

import android.app.Application
import com.example.savingmoney.utils.NotificationUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Tạo Notification Channel ngay khi app khởi chạy
        NotificationUtils.createNotificationChannel(this)
    }
}
