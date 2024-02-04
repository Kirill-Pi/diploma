package com.example.diploma.utils

import com.example.diploma.data.Launch
import com.example.diploma.data.TmdbLaunch

object ConverterApi {

    fun converterOfLaunches (list: TmdbLaunch?): MutableList<Launch> {
        val result = mutableListOf<Launch>()
        list?.results?.forEach {
            result.add(
                Launch(
                    name = it.name,
                    net = it.net.dropLast(10),
                    launchServiceProviderName = it.launch_service_provider.name,
                    rocketFullName = it.rocket.configuration.full_name,
                    missionName = it.mission.name,
                    missionDescription = it.mission.description,
                    padName = it.pad.name,
                    padLocation = it.pad.country_code,
                    rocket = it.rocket.configuration.full_name,
                    image = it.image
                )
            )
        }
        return result
    }

}