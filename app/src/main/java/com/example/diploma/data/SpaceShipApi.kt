package com.example.diploma.data

data class SpaceShipApi(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Result>
) {
    data class Result(
        val description: String,
        val flights_count: Int,
        val id: Int,
        val in_space: Boolean,
        val is_placeholder: Boolean,
        val mission_ends_count: Int,
        val name: String,
        val serial_number: String,
        val spacecraft_config: SpacecraftConfig,
        val status: Status,
        val time_docked: String,
        val time_in_space: String,
        val url: String
    ) {
        data class SpacecraftConfig(
            val agency: Agency,
            val id: Int,
            val image_url: String = "",
            val in_use: Boolean,
            val name: String,
            val type: Type,
            val url: String
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

        data class Status(
            val id: Int,
            val name: String
        )
    }
}