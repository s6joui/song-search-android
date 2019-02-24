package tech.joeyck.songsearch.data

import java.util.*

data class TrackResult(val trackId : Int,
                       val artistName : String?,
                       val trackName: String?,
                       val collectionName : String?,
                       val artworkUrl100 : String?,
                       val previewUrl : String?,
                       val primaryGenreName: String?,
                       val releaseDate : Date?,
                       val trackTimeMillis : Int?,
                       val trackPrice : Float?,
                       val currency: String?,
                       val trackViewUrl: String?){

    companion object {
        fun empty() : TrackResult{
            return TrackResult(
                0,
                "",
                ",",
                "",
                "",
                "",
                "",
                Date(),
                0,
                0f,
                "",
                ""
            )
        }
    }

}