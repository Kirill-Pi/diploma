package com.example.diploma.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diploma.App
import com.example.diploma.domain.Interactor
import javax.inject.Inject

class SettingsViewModel : ViewModel() {
    //Инжектим интерактор
    @Inject
    lateinit var interactor: Interactor
    val mannedPropertyLifeData: MutableLiveData<Int> = MutableLiveData()
    val inUsePropertyLifeData: MutableLiveData<Int> = MutableLiveData()
    val countryPropertyLifeData: MutableLiveData<String> = MutableLiveData()

    init {
        App.instance.dagger.inject(this)
        //Получаем категорию при инициализации, чтобы у нас сразу подтягивалась категория
        //getCountryProperty()
        getIsInUseProperty()
        getIsMannedProperty()
    }

    private fun getIsMannedProperty() {
        //Кладем категорию в LiveData
        mannedPropertyLifeData.value = interactor.getIsMannedFromPreferences()
    }

    fun putIsMannedProperty(isManned: Int) {
        //Сохраняем в настройки
        interactor.saveIsManned(isManned)
        //И сразу забираем, чтобы сохранить состояние в модели
        getIsMannedProperty()
    }

    private fun getIsInUseProperty() {
        //Кладем категорию в LiveData
        inUsePropertyLifeData.value = interactor.getIsInUseFromPreferences()
    }

    fun putIsInUseProperty(isInUse: Int) {
        //Сохраняем в настройки
        interactor.saveIsInUse(isInUse)
        //И сразу забираем, чтобы сохранить состояние в модели
        getIsInUseProperty()
    }


    private fun getCountryProperty() {
        //Кладем категорию в LiveData
        countryPropertyLifeData.value = interactor.getCountryFromPreferences()
    }

    fun putCountryProperty(country: String) {
        //Сохраняем в настройки
        interactor.saveCountryToPreferences(country)
        //И сразу забираем, чтобы сохранить состояние в модели
        getCountryProperty()
    }
}