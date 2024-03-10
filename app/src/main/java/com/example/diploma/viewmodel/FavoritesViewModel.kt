package com.example.diploma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.diploma.App
import com.example.diploma.data.Favorites
import com.example.diploma.domain.Interactor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesViewModel : ViewModel() {

    val favoritesListLiveData: Flow<MutableList<Favorites>>

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        favoritesListLiveData = interactor.getFavoritesFromDB()

    }

}