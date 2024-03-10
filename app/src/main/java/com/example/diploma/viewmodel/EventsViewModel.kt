package com.example.diploma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.diploma.App
import com.example.diploma.data.Events
import com.example.diploma.domain.Interactor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventsViewModel : ViewModel() {

    val eventsListLiveData: Flow<MutableList<Events>>
    private var offset = 0
    private var offsetValue = 10
    private var searchQuery = ""

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        eventsListLiveData = interactor.getEventsFromDB()


    }

    fun offsetSetup(){
        offset = 0
    }

    fun searchQuerySetUp(query:String){
        searchQuery = query
    }

    fun getEvents(){
        interactor.getEventsFromApi( object : ApiCallback {
            override fun onSuccess(events: MutableList<Events>) {

            }
            override fun onFailure() {
            }
        }, searchQuery,offset)
    }

    fun nextPage () {
        offset += offsetValue
        getEvents()
    }

    interface ApiCallback {
        fun onSuccess(events: MutableList<Events>)
        fun onFailure()
    }
}