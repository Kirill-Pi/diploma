package com.example.diploma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.diploma.App
import com.example.diploma.data.*
import com.example.diploma.domain.Interactor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpaceShipsViewModel : ViewModel() {

    val spaceCraftsListLiveData: Flow<MutableList<SpacecraftConfig>>
    val lastSeenListLiveData: Flow<MutableList<RecentlySeen>>
    val favoritesListLiveData: Flow<MutableList<Favorites>>

    private var offset = 0
    private var offsetValue = 10
    private var isFirst = true
    private var searchQuery = ""

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        getSpacecrafts ()
        spaceCraftsListLiveData = interactor.getSpacecraftsFromDB()
        lastSeenListLiveData = interactor.getRecentlySeenFromDB()
        favoritesListLiveData = interactor.getFavoritesFromDB()
        isFirst = false
    }

    fun offsetSetup(){
        offset = 0
    }

    fun searchQuerySetUp(query:String){
        searchQuery = query
    }

    fun getSpacecrafts () = interactor.getSpaceShipFromApi(object :
        ApiCallback {
        override fun onSuccess(ships: MutableList<SpacecraftConfig>) {
        }
        override fun onFailure() {
        }
    }, searchQuery, offset, isFirst)

    fun nextPage () {
        offset += offsetValue
        getSpacecrafts()
    }

    interface ApiCallback {
        fun onSuccess(ships: MutableList<SpacecraftConfig>)
        fun onFailure()
    }
}