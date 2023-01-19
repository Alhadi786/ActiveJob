package com.xee.app.activejob

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ActiveJobApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}