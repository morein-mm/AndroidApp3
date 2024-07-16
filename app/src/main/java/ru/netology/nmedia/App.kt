package ru.netology.nmedia

import android.app.Application
import ru.netology.nmedia.auth.AppAuth

class App : Application() {
    private lateinit var container: DependencyContainer

    override fun onCreate() {
        super.onCreate()
        DependencyContainer.initApp(this)
        AppAuth.init(this)
    }
}