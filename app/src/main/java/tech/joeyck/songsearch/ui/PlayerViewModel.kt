package tech.joeyck.songsearch.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import tech.joeyck.songsearch.App
import tech.joeyck.songsearch.data.TrackRepository
import tech.joeyck.songsearch.data.TrackResult
import javax.inject.Inject

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var trackRepository : TrackRepository

    val track : MediatorLiveData<TrackResult> = MediatorLiveData()
    var trackIndex : Int = 0
    var searchQuery : String = ""

    init{
        getApplication<App>().appComponent.inject(this)
        track.addSource(trackRepository.searchResults){
            track.value = it[trackIndex]
        }
    }

    // MediaPlayer UI state
    enum class MediaPlayerState{
        NOT_READY,LOADING,PLAYING,PAUSED
    }

    val playerState : MutableLiveData<MediaPlayerState> = MutableLiveData(MediaPlayerState.NOT_READY);

    fun setPlayerState(state: MediaPlayerState){
        playerState.postValue(state)
    }

    // Track repository
    fun fetchTrack(index: Int){
        trackIndex = index
        if(trackRepository.hasResults()){
            track.postValue(trackRepository.getTrack(index))
        }else{
            trackRepository.search(searchQuery)
        }
    }

    fun nextTrack() : Int{
        if(trackIndex < trackRepository.searchResultCount - 1){
            fetchTrack(trackIndex + 1)
            return trackIndex + 1
        }
        return trackIndex
    }

    fun prevTrack() : Int{
        if(trackIndex > 0){
            fetchTrack(trackIndex - 1)
            return trackIndex - 1
        }
        return trackIndex
    }

    fun results() : LiveData<Array<TrackResult>> = trackRepository.searchResults

}
