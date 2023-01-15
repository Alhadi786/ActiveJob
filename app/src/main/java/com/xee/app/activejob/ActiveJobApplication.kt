package com.xee.app.activejob

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ActiveJobApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(applicationContext)
    }
}