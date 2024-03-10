package com.example.diploma.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceProvider(context: Context) {
    //Нам нужен контекст приложения
    private val appContext = context.applicationContext
    //Создаем экземпляр SharedPreferences
    private val preference: SharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    init {
        //Логика для первого запуска приложения, чтобы положить наши настройки,
        //Сюда потом можно добавить и другие настройки
        if(preference.getBoolean(KEY_FIRST_LAUNCH, false)) {
            preference.edit { putString(KEY_COUNTRY, COUNTRY) }
            preference.edit { putInt(KEY_IS_MANNED, 2) }
            preference.edit { putInt(KEY_IS_IN_USE_2, 2) }

        }
    }

    //Category prefs
    //Сохраняем категорию
    fun saveCountry(country: String) {
        preference.edit { putString(KEY_COUNTRY, country) }
    }
    //Забираем категорию
    fun getCountry(): String {
        return preference.getString(KEY_COUNTRY, COUNTRY) ?: COUNTRY
    }
    fun saveIsManned(isManned: Int) {
        preference.edit { putInt(KEY_IS_MANNED, isManned) }
    }
    //Забираем категорию
    fun getIsManned(): Int {
        return preference.getInt(KEY_IS_MANNED, IS_MANNED)
    }
    fun saveIsInUse(isInUse: Int) {
        preference.edit { putInt(KEY_IS_IN_USE_2, isInUse) }
    }
    //Забираем категорию
    fun getIsInUse(): Int {
        return preference.getInt(KEY_IS_IN_USE_2, IS_IN_USE)
    }

    //Ключи для наших настроек, по ним мы их будем получать
    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_IS_MANNED = "is_manned"
        private const val IS_MANNED = 2
        private const val KEY_IS_IN_USE_2 = "is_in_use"
        private const val IS_IN_USE = 2
        private const val KEY_COUNTRY = "country"
        private const val COUNTRY = "USA"
    }
}