package com.example.diploma.data

data class TmdbLaunch(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>?
) {
    data class Result(
        val agency_launch_attempt_count: Int,
        val agency_launch_attempt_count_year: Int,
        val failreason: String,
        val hashtag: Any,
        val holdreason: String,
        val id: String,
        val image: String ="",
        val infographic: Any,
        val last_updated: String,
        val launch_service_provider: LaunchServiceProvider,
        val location_launch_attempt_count: Int,
        val location_launch_attempt_count_year: Int,
        val mission: Mission,
        val name: String,
        val net: String,
        val net_precision: NetPrecision,
        val orbital_launch_attempt_count: Int,
        val orbital_launch_attempt_count_year: Int,
        val pad: Pad,
        val pad_launch_attempt_count: Int,
        val pad_launch_attempt_count_year: Int,
        val probability: Int,
        val program: List<Program>,
        val rocket: Rocket,
        val slug: String,
        val status: Status,
        val type: String,
        val url: String,
        val weather_concerns: Any,
        val webcast_live: Boolean,
        val window_end: String,
        val window_start: String
    ) {
        data class LaunchServiceProvider(
            val id: Int,
            val name: String,
            val type: String,
            val url: String
        )

        data class Mission(
            val agencies: List<Any>,
            val description: String,
            val id: Int,
            val info_urls: List<Any>,
            val launch_designator: Any,
            val name: String,
            val orbit: Orbit,
            val type: String,
            val vid_urls: List<Any>
        ) {
            data class Orbit(
                val abbrev: String,
                val id: Int,
                val name: String
            )
        }

        data class NetPrecision(
            val abbrev: String,
            val description: String,
            val id: Int,
            val name: String
        )

        data class Pad(
            val agency_id: Int,
            val country_code: String,
            val description: Any,
            val id: Int,
            val info_url: Any,
            val latitude: String,
            val location: Location,
            val longitude: String,
            val map_image: String,
            val map_url: String,
            val name: String,
            val orbital_launch_attempt_count: Int,
            val total_launch_count: Int,
            val url: String,
            val wiki_url: String
        ) {
            data class Location(
                val country_code: String,
                val description: Any,
                val id: Int,
                val map_image: String,
                val name: String,
                val timezone_name: String,
                val total_landing_count: Int,
                val total_launch_count: Int,
                val url: String
            )
        }

        data class Program(
            val agencies: List<Agency>,
            val description: String,
            val end_date: Any,
            val id: Int,
            val image_url: String,
            val info_url: String,
            val mission_patches: List<Any>,
            val name: String,
            val start_date: String,
            val type: Type,
            val url: String,
            val wiki_url: String
        ) {
            data class Agency(
                val id: Int,
                val name: String,
                val type: String,
                val url: String
            )

            data class Type(
                val id: Int,
                val name: String
            )
        }

        data class Rocket(
            val configuration: Configuration,
            val id: Int
        ) {
            data class Configuration(
                val family: String,
                val full_name: String,
                val id: Int,
                val name: String,
                val url: String,
                val variant: String
            )
        }

        data class Status(
            val abbrev: String,
            val description: String,
            val id: Int,
            val name: String
        )
    }
}