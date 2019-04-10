package se.knowit.doggos

import android.app.Application

class DoggosApp: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: DoggosApp? = null
        fun getInstance() = instance
    }
}
