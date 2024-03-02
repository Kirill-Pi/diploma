package com.example.diploma.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.icu.util.Calendar
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.diploma.data.ApiConstants
import com.example.diploma.data.Events
import com.example.diploma.receivers.ReminderBroadcast
import com.example.diploma.view.MainActivity
import com.example.pigolevmyapplication.R

object NotificationHelper {
    fun createNotification(context: Context, event: Events) {
        val mIntent = Intent(context, MainActivity::class.java)

        val pendingIntent =
            PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID).apply {
            setSmallIcon(R.drawable.baseline_watch_later_24)
            setContentTitle("Do not forget to see Event!")
            setContentText(event.name)
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        val notificationManager = NotificationManagerCompat.from(context)

        Glide.with(context)
            //говорим что нужен битмап
            .asBitmap()
            //указываем откуда загружать, это ссылка как на загрузку с API
            .load( event.image)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }
                //Этот коллбэк отрабатоет когда мы успешно получим битмап
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    //Создаем нотификации в стиле big picture
                    builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                    //Обновляем нотификацю
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        return
                    }
                    println("notify")
                    notificationManager.notify(event.id, builder.build())
                }
            })
        //Отправляем изначальную нотификацю в стандартном исполнении
        println("notify")
        notificationManager.notify(event.id, builder.build())
    }

    fun notificationSet(context: Context, event: Events) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        DatePickerDialog(
            context,
            { _, dpdYear, dpdMonth, dayOfMonth ->
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, pickerMinute ->
                        val pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(
                            dpdYear,
                            dpdMonth,
                            dayOfMonth,
                            hourOfDay,
                            pickerMinute,
                            0
                        )
                        val dateTimeInMillis = pickedDateTime.timeInMillis
                        //После того, как получим время, вызываем метод, который создаст Alarm
                        createWatchLaterEvent(context, dateTimeInMillis, event)
                    }

                TimePickerDialog(
                    context,
                    timeSetListener,
                    currentHour,
                    currentMinute,
                    true
                ).show()

            },
            currentYear,
            currentMonth,
            currentDay
        ).show()
    }

    private fun createWatchLaterEvent(context: Context, dateTimeInMillis: Long, event: Events) {
        //Получаем доступ к AlarmManager
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //Создаем интент для запуска ресивера
        val intent = Intent(event.name, null, context, ReminderBroadcast()::class.java)

        val bundle = Bundle()
        bundle.putParcelable(NotificationConstants.EVENT_KEY, event)
        intent.putExtra(NotificationConstants.EVENT_BUNDLE_KEY, bundle)
        //Создаем пендинг интент для запуска извне приложения
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            //PendingIntent.FLAG_MUTABLE
        )
        //Устанавливаем Alarm
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            dateTimeInMillis,
            pendingIntent
        )
    }
}