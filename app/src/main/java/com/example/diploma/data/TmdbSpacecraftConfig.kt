package com.example.diploma.data

data class TmdbSpacecraftConfig(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
) {
    data class Result(
        val agency: Agency,
        val capability: String,
        val crew_capacity: Int,
        val human_rated: Boolean,
        val id: Int,
        val image_url: String ="",
        val in_use: Boolean,
        val info_link: String,
        val maiden_flight: String,
        val name: String,
        val nation_url: String = "",
        val url: String,
        val wiki_link: String
    ) {
        data class Agency(
            val abbrev: String,
            val administrator: String,
            val country_code: String,
            val description: String,
            val featured: Boolean,
            val founding_year: String,
            val id: Int,
            val image_url: String ?,
            val launchers: String,
            val logo_url: String,
            val name: String,
            val parent: String,
            val spacecraft: String,
            val type: String,
            val url: String
        )
    }
}