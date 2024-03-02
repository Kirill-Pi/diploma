package com.example.diploma.data

data class TmdbEvents(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
) {
    data class Result(
        val agencies: List<Any>,
        val date: String,
        val date_precision: Any,
        val description: String,
        val duration: Any,
        val expeditions: List<Expedition>,
        val feature_image: String,
        val id: Int,
        val info_urls: List<InfoUrl>,
        val last_updated: String,
        val launches: List<Any>,
        val location: String,
        val name: String,
        val news_url: String,
        val program: List<Program>,
        val slug: String,
        val spacestations: List<Spacestation>,
        val type: Type,
        val updates: List<Any>,
        val url: String,
        val vid_urls: List<VidUrl>,
        val video_url: String,
        val webcast_live: Boolean
    ) {
        data class Expedition(
            val end: String,
            val id: Int,
            val mission_patches: List<MissionPatche>,
            val name: String,
            val spacestation: Spacestation,
            val spacewalks: List<Spacewalk>,
            val start: String,
            val url: String
        ) {
            data class MissionPatche(
                val agency: Agency,
                val id: Int,
                val image_url: String,
                val name: String,
                val priority: Int
            ) {
                data class Agency(
                    val id: Int,
                    val name: String,
                    val type: String,
                    val url: String
                )
            }

            data class Spacestation(
                val id: Int,
                val image_url: String,
                val name: String,
                val orbit: String,
                val status: Status,
                val url: String
            ) {
                data class Status(
                    val id: Int,
                    val name: String
                )
            }

            data class Spacewalk(
                val duration: String,
                val end: String,
                val id: Int,
                val location: String,
                val name: String,
                val start: String,
                val url: String
            )
        }

        data class InfoUrl(
            val description: String,
            val feature_image: Any,
            val language: Language,
            val priority: Int,
            val source: String,
            val title: String,
            val type: Any,
            val url: String
        ) {
            data class Language(
                val code: String,
                val id: Int,
                val name: String
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

        data class Spacestation(
            val description: String,
            val founded: String,
            val id: Int,
            val image_url: String,
            val name: String,
            val orbit: String,
            val status: Status,
            val url: String
        ) {
            data class Status(
                val id: Int,
                val name: String
            )
        }

        data class Type(
            val id: Int,
            val name: String
        )

        data class VidUrl(
            val description: String,
            val end_time: Any,
            val feature_image: String,
            val language: Language,
            val priority: Int,
            val publisher: Any,
            val source: String,
            val start_time: Any,
            val title: String,
            val type: Type,
            val url: String
        ) {
            data class Language(
                val code: String,
                val id: Int,
                val name: String
            )

            data class Type(
                val id: Int,
                val name: String
            )
        }
    }
}