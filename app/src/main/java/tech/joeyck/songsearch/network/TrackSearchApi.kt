package tech.joeyck.songsearch.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import tech.joeyck.songsearch.data.SearchResult

interface TrackSearchApi {

    @GET("/search")
    fun getResults(@Query("term") query : String) : Call<SearchResult>

}