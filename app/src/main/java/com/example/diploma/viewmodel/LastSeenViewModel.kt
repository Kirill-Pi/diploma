package com.example.diploma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.diploma.App
import com.example.diploma.data.SpacecraftConfig
import com.example.diploma.domain.Interactor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LastSeenViewModel : ViewModel() {

    val lastSeenListLiveData: Flow<MutableList<SpacecraftConfig>>
    private var offset = 0
    private var offsetValue = 10

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        lastSeenListLiveData = interactor.getLastSeenFromDB()
        //interactorStart()
    }


}