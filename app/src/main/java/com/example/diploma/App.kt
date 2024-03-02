package com.example.diploma

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.diploma.di.AppComponent
import com.example.diploma.di.DaggerAppComponent

import com.example.diploma.di.modules.DatabaseModule
import com.example.diploma.di.modules.DomainModule

import com.example.diploma.di.modules.RemoteModule
import com.example.diploma.notifications.NotificationConstants.CHANNEL_ID

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

        //Задаем имя, описание и важность канала
        val name = "EventsSearchChannel"
        val descriptionText = "Events notification Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        //Создаем канал, передав в параметры его ID(строка), имя(строка), важность(константа)
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        //Отдельно задаем описание
        mChannel.description = descriptionText
        //Получаем доступ к менеджеру нотификаций
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //Регистрируем канал
        notificationManager.createNotificationChannel(mChannel)
    }




    companion object {
        lateinit var instance: App
            private set
    }
}