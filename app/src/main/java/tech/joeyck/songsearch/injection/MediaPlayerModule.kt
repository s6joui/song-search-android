package tech.joeyck.songsearch.injection

import android.media.MediaPlayer
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object MediaPlayerModule {

    @Provides
    @JvmStatic
    fun provideMediaPlayer() : MediaPlayer{
        return MediaPlayer()
    }

}