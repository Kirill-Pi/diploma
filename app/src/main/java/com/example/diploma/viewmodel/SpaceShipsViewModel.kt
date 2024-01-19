package com.example.diploma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.diploma.App
import com.example.diploma.domain.Interactor
import com.example.diploma.data.TmdbSpacecraftConfig
import javax.inject.Inject

class SpaceShipsViewModel : ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        interactorStart()
    }

    fun interactorStart(){
        interactor.getSpaceShipFromApi( object : ApiCallback {
            override fun onSuccess(ships: TmdbSpacecraftConfig) {
               // coursesListLiveData.postValue(courses)
            }

            override fun onFailure() {
            }
        })
    }

    interface ApiCallback {
        fun onSuccess(ships: TmdbSpacecraftConfig)
        fun onFailure()
    }
}