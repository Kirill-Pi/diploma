package com.example.diploma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.diploma.App
import com.example.diploma.data.Launch
import com.example.diploma.domain.Interactor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LaunchesViewModel : ViewModel() {
    val launchListLiveData: Flow<MutableList<Launch>>
    private var offset = 0
    private var offsetValue = 10

    @Inject
    lateinit var interactor: Interactor



    init {
        App.instance.dagger.inject(this)

        launchListLiveData = interactor.getLaunchesFromDB()
    }

    fun offsetSetup(){
        offset = 0
    }

    fun getLaunches (date: String ) = interactor.getLaunchesFromApi(object : ApiCallback {
        override fun onSuccess(events: MutableList<Launch>) {

        }

        override fun onFailure() {
        }
    }, date, offset)

    fun nextPage (date: String) {

        offset += offsetValue
        getLaunches(date)

    }

    fun cleanDb(){
        interactor.cleanDb()
    }


    interface ApiCallback {
        fun onSuccess(events: MutableList<Launch>)
        fun onFailure()
    }
}
