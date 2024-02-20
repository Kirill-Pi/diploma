package com.example.diploma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.diploma.App
import com.example.diploma.data.Launch
import com.example.diploma.data.SpacecraftConfig
import com.example.diploma.domain.Interactor
import com.example.diploma.data.TmdbSpacecraftConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpaceShipsViewModel : ViewModel() {

    val spaceCraftsListLiveData: Flow<MutableList<SpacecraftConfig>>
    private var offset = 0
    private var offsetValue = 10

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        spaceCraftsListLiveData = interactor.getSpacecraftsFromDB()
        //interactorStart()
    }

    fun getSpacecrafts () = interactor.getSpaceShipFromApi(object :
        SpaceShipsViewModel.ApiCallback {
        override fun onSuccess(events: MutableList<SpacecraftConfig>) {

        }

        override fun onFailure() {
        }
    }, offset)

    fun nextPage () {

        offset += offsetValue
        getSpacecrafts()

    }



    interface ApiCallback {
        fun onSuccess(ships: MutableList<SpacecraftConfig>)
        fun onFailure()
    }
}