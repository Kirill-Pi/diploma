package com.example.diploma.di

import com.example.diploma.di.modules.DatabaseModule
import com.example.diploma.di.modules.DomainModule
import com.example.diploma.di.modules.RemoteModule
import com.example.diploma.viewmodel.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    //Внедряем все модули, нужные для этого компонента
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class
    ]
)
interface AppComponent {

    fun inject(eventsViewModel: EventsViewModel)

    fun inject(launchesViewModel: LaunchesViewModel)

    fun inject(spaceShipsViewModel: SpaceShipsViewModel)

    fun inject(settingsViewModel: SettingsViewModel)

    fun inject(favoritesViewModel: FavoritesViewModel)

    fun inject(detailsSCFragmentViewModel: DetailsSCFragmentViewModel)

    fun inject(lastSeenViewModel: LastSeenViewModel)
}