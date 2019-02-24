package tech.joeyck.songsearch

import android.app.Application
import tech.joeyck.songsearch.injection.AppComponent
import tech.joeyck.songsearch.injection.AppModule
import tech.joeyck.songsearch.injection.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }

}