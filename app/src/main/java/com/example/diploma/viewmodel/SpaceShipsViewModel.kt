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
    val lastSeenListLiveData: Flow<MutableList<SpacecraftConfig>>
    private var offset = 0
    private var offsetValue = 10
    private var isFirst = true

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        getSpacecrafts ()
        spaceCraftsListLiveData = interactor.getSpacecraftsFromDB()
        lastSeenListLiveData = interactor.getLastSeenFromDB()
        isFirst = false
    }

    fun getSpacecrafts () = interactor.getSpaceShipFromApi(object :
        ApiCallback {
        override fun onSuccess(ships: MutableList<SpacecraftConfig>) {

        }

        override fun onFailure() {
        }
    }, offset, isFirst)

    fun nextPage () {

        offset += offsetValue
        getSpacecrafts()

    }



    interface ApiCallback {
        fun onSuccess(ships: MutableList<SpacecraftConfig>)
        fun onFailure()
    }
}