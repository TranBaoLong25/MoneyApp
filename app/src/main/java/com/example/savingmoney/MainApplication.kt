package com.example.savingmoney

import android.app.Application
import dagger.hilt.android.HiltAndroidApp // Import Hilt

@HiltAndroidApp // THÊM DÒNG NÀY
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Init stuff (Hilt, logging, etc.)
    }
}