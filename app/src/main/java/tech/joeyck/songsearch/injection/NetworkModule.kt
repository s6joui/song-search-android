package tech.joeyck.songsearch.injection

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tech.joeyck.songsearch.network.TrackSearchApi
import tech.joeyck.songsearch.utils.SONG_SEARCH_API_BASE_URL
import javax.inject.Singleton
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import java.util.*


@Module
object NetworkModule{

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideTrackSearchAPI(retrofit: Retrofit) : TrackSearchApi {
        return retrofit.create(TrackSearchApi::class.java)
    }

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideRetrofit(moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(SONG_SEARCH_API_BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()
    }

}