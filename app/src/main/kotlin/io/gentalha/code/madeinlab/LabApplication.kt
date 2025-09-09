package io.gentalha.code.madeinlab

import android.app.Application
import io.gentalha.code.madeinlab.feature.login.di.LoginModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

class LabApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@LabApplication)
            modules(LoginModule().module)
        }
    }
}