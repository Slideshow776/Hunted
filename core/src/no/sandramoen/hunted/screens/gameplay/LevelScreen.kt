package no.sandramoen.hunted.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.BaseScreen

class LevelScreen: BaseScreen() {
    private val tag = "LevelScreen"

    override fun initialize() {
        Gdx.app.error(tag,"level screen initialize()")
    }

    override fun update(dt: Float) {}
}
