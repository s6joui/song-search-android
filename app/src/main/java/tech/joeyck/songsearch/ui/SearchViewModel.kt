package tech.joeyck.songsearch.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import tech.joeyck.songsearch.App
import tech.joeyck.songsearch.data.TrackRepository
import tech.joeyck.songsearch.data.TrackResult
import tech.joeyck.songsearch.utils.sortByTrackOrder
import javax.inject.Inject

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var trackRepository: TrackRepository
    private val searchResults = MediatorLiveData<Array<TrackResult>>()

    var currentOrder = 0
    var searchQuery: String = ""

    init {
        getApplication<App>().appComponent.inject(this)
        searchResults.addSource(trackRepository.searchResults) { results: Array<TrackResult> ->
            results.sortByTrackOrder(currentOrder)
            searchResults.value = results
        }
    }

    fun setQuery(query: String) {
        currentOrder = 0
        searchQuery = query
        trackRepository.search(query)
    }

    fun results(): LiveData<Array<TrackResult>> = searchResults

    fun error(): LiveData<String> = trackRepository.searchError

    fun sortResultsBy(sortId: Int) {
        currentOrder = sortId
        val results = trackRepository.searchResults.value
        results.sortByTrackOrder(currentOrder)
        searchResults.postValue(results)
    }

}