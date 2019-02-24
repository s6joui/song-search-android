package tech.joeyck.songsearch.injection

import dagger.Component
import tech.joeyck.songsearch.ui.PlayerActivity

@Component(modules = [MediaPlayerModule::class])
interface MediaComponent {

    fun inject(playerActivity: PlayerActivity)

}