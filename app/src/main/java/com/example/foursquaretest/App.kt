package com.example.foursquaretest

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {

        lateinit var appInstance: App
        val appContext: Context by lazy {
            appInstance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this

    }
}