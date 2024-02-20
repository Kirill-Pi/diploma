package com.example.diploma.utils

import com.example.diploma.data.Launch
import com.example.diploma.data.SpacecraftConfig
import com.example.diploma.data.TmdbLaunch
import com.example.diploma.data.TmdbSpacecraftConfig

object ConverterApi {

    fun converterOfLaunches(list: TmdbLaunch?): MutableList<Launch> {
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

    fun converterOfSpacecrafts(list: TmdbSpacecraftConfig?): MutableList<SpacecraftConfig> {
        val result = mutableListOf<SpacecraftConfig>()
        list?.results?.forEach {
            result.add(
                SpacecraftConfig(
                    agency = it.agency.name,
                    capability = it.capability,
                    crewCapacity = it.crew_capacity,
                    humanRated = it.human_rated,
                    imageUrl = it.image_url,
                    inUse = it.in_use,
                    maidenFlight = it.maiden_flight,
                    name = it.name,
                    countryCode = it.agency.country_code,
                  //  nationUrl = it.nation_url,
                )
            )
        }
        return result
    }

}