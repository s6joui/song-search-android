package tech.joeyck.songsearch.data

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.joeyck.songsearch.network.TrackSearchApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackRepository @Inject constructor(private val trackSearchApi: TrackSearchApi) {

    val searchResults: MutableLiveData<Array<TrackResult>> = MutableLiveData()
    val searchError: MutableLiveData<String> = MutableLiveData()
    var searchResultCount : Int = 0
    var searchRequest : Call<SearchResult>? = null

    fun search(query: String) {
        searchRequest?.cancel()
        if(query.isEmpty()){
            searchError.postValue("No results")
            return
        }
        searchRequest = trackSearchApi.getResults(query)
        searchRequest!!.enqueue(object: Callback<SearchResult> {
            override fun onResponse(call: retrofit2.Call<SearchResult>, response: Response<SearchResult>) {
                response.body()?.let{
                    searchResultCount = it.resultCount
                    searchResults.postValue(it.results)
                }
            }
            override fun onFailure(call: retrofit2.Call<SearchResult>, t: Throwable) {
                if(!call.isCanceled) searchError.postValue(t.localizedMessage)
            }
        })
    }

    fun getTrack(index: Int): TrackResult {
        return searchResults.value?.get(index) ?: TrackResult.empty()
    }

    fun hasResults(): Boolean{
        return searchResultCount > 0
    }

}