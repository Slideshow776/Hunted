package no.sandramoen.hunted

import no.sandramoen.hunted.screens.gameplay.Level1Screen
import no.sandramoen.hunted.screens.shell.MenuScreen
import no.sandramoen.hunted.screens.shell.OptionsScreen
import no.sandramoen.hunted.screens.shell.SplashScreen
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.GooglePlayServices

class HuntedGame(googlePlayServices: GooglePlayServices?, appLocale: String) : BaseGame(googlePlayServices, appLocale) {
    override fun create() {
        super.create()
        // setActiveScreen(Level1Screen())
        // setActiveScreen(MenuScreen())
        // setActiveScreen(OptionsScreen())
        setActiveScreen(SplashScreen())
    }
}