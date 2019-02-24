package tech.joeyck.songsearch.injection

import dagger.Component
import tech.joeyck.songsearch.ui.PlayerViewModel
import tech.joeyck.songsearch.ui.SearchViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,NetworkModule::class])
interface AppComponent {

    fun inject(playerViewModel: PlayerViewModel)

    fun inject(searchViewModel: SearchViewModel)

}