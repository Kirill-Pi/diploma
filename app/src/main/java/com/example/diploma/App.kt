package com.example.diploma

import android.app.Application
import com.airbnb.lottie.BuildConfig
import com.example.diploma.data.ApiConstants
import com.example.diploma.data.MainRepository
import com.example.diploma.data.TmdbApi
import com.example.diploma.di.AppComponent
import com.example.diploma.di.DaggerAppComponent
import com.example.diploma.di.modules.DatabaseModule
import com.example.diploma.di.modules.DomainModule

import com.example.diploma.di.modules.RemoteModule
import com.example.diploma.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.builder()
            .remoteModule(RemoteModule())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
    }


    companion object {
        lateinit var instance: App
            private set
    }
}