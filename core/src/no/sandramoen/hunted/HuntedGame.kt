package no.sandramoen.hunted

import no.sandramoen.hunted.screens.gameplay.LevelScreen
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.GooglePlayServices

class HuntedGame(googlePlayServices: GooglePlayServices?, appLocale: String) : BaseGame(googlePlayServices, appLocale) {
    override fun create() {
        super.create()
        setActiveScreen(LevelScreen())
    }
}