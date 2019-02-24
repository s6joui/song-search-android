package tech.joeyck.songsearch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_song_detail.*
import tech.joeyck.songsearch.R
import android.media.MediaPlayer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import tech.joeyck.songsearch.injection.DaggerMediaComponent
import tech.joeyck.songsearch.ui.PlayerViewModel.*
import tech.joeyck.songsearch.ui.PlayerViewModel.MediaPlayerState.*
import tech.joeyck.songsearch.utils.ImageLoader
import tech.joeyck.songsearch.utils.yearString
import javax.inject.Inject

class PlayerActivity : AppCompatActivity() {

    @Inject lateinit var mediaPlayer: MediaPlayer
    private lateinit var playerViewModel : PlayerViewModel

    private var currentTrackPreviewUrl = ""

    private var mediaPlayerState : MediaPlayerState = NOT_READY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_detail)
        supportActionBar?.elevation = 0f
        title = ""
        image_play.isEnabled = false
        image_prev.isEnabled = false
        image_next.isEnabled = false

        DaggerMediaComponent.create().inject(this)

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)

        playerViewModel.track.observe(this, Observer {
            text_album.text = getString(R.string.album_title,it.collectionName,it.releaseDate?.yearString())
            text_artist.text = it.artistName
            text_track.text = it.trackName
            ImageLoader.loadImage(it.artworkUrl100,image_art)
            currentTrackPreviewUrl = it.previewUrl ?: ""
            image_play.isEnabled = true
            image_prev.isEnabled = true
            image_next.isEnabled = true
            progress.visibility = View.GONE
        })

        playerViewModel.playerState.observe(this, Observer { mediaPlayerState ->
            this.mediaPlayerState = mediaPlayerState
            when(mediaPlayerState!!){
                LOADING -> {
                    image_play.isEnabled = false
                    streamProgress.visibility = View.VISIBLE
                }
                PLAYING -> {
                    image_play.isEnabled = true
                    streamProgress.visibility = View.INVISIBLE
                    image_play.setImageResource(R.drawable.ic_pause)

                }
                NOT_READY, PAUSED -> image_play.setImageResource(R.drawable.ic_play)
            }
        })

        image_play.setOnClickListener {
            when (mediaPlayerState) {
                NOT_READY -> setupPlayer(currentTrackPreviewUrl)
                PLAYING -> {
                    mediaPlayer.pause()
                    playerViewModel.setPlayerState(PAUSED)
                }
                PAUSED -> {
                    mediaPlayer.start()
                    playerViewModel.setPlayerState(PLAYING)
                }
                else -> println(mediaPlayer)
            }
        }

        image_next.setOnClickListener{
            playerViewModel.nextTrack()
            resetPlayer()
        }

        image_prev.setOnClickListener{
            playerViewModel.prevTrack()
            resetPlayer()
        }

        loadState(intent.extras)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        playerViewModel.fetchTrack(playerViewModel.trackIndex)
    }

    private fun setupPlayer(dataSource: String){
        if(dataSource.isEmpty()){
            return
        }
        playerViewModel.setPlayerState(LOADING)
        mediaPlayer.isLooping = true
        mediaPlayer.setDataSource(dataSource)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            it.start()
            playerViewModel.setPlayerState(PLAYING)
        }
    }

    private fun resetPlayer(){
        streamProgress.visibility = View.INVISIBLE
        mediaPlayer.stop()
        mediaPlayer.reset()
        playerViewModel.setPlayerState(NOT_READY)
    }

    private fun loadState(bundle: Bundle?) {
        playerViewModel.searchQuery = bundle?.getString(SearchActivity.SEARCH_QUERY) ?: ""
        playerViewModel.trackIndex = bundle?.getInt(SearchActivity.TRACK_INDEX) ?: -1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SearchActivity.TRACK_INDEX,playerViewModel.trackIndex)
        outState.putString(SearchActivity.SEARCH_QUERY,playerViewModel.searchQuery)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        loadState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_player,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.share){
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.share_track)
                .setText(playerViewModel.track.value?.trackViewUrl)
                .startChooser()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onDestroy()
    }

}
