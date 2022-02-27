package no.sandramoen.hunted.screens.shell

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import no.sandramoen.hunted.actors.ShockwaveBackground
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.BaseScreen

class SplashScreen : BaseScreen() {
    private lateinit var shockwaveBackground: ShockwaveBackground
    private lateinit var blackOverlay: BaseActor

    override fun initialize() {
        shockwaveBackground = ShockwaveBackground("images/excluded/splash.jpg", mainStage)
        blackOverlayAnimation()
    }

    override fun update(dt: Float) {}

    override fun dispose() {
        super.dispose()
        shockwaveBackground.shaderProgram.dispose()
        shockwaveBackground.remove()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACKSPACE)
            Gdx.app.exit()
        return false
    }

    private fun blackOverlayAnimation() {
        blackOverlayInitialization()
        blackOverlayFadeInAndOut()
        disposeAndSetActiveScreen()
    }

    private fun blackOverlayInitialization() {
        blackOverlay = BaseActor(0f, 0f, mainStage)
        blackOverlay.loadImage("whitePixel")
        blackOverlay.color = Color.BLACK
        blackOverlay.touchable = Touchable.childrenOnly
        blackOverlay.setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT)
    }

    private fun blackOverlayFadeInAndOut() {
        val totalDurationInSeconds = 4f
        blackOverlay.addAction(
            Actions.sequence(
                Actions.fadeIn(0f),
                Actions.fadeOut(totalDurationInSeconds / 3),
                Actions.run { googlePlayServicesSignIn() },
                Actions.delay(totalDurationInSeconds / 3),
                Actions.fadeIn(totalDurationInSeconds / 3)
            )
        )
    }

    private fun googlePlayServicesSignIn() {
        if (
            Gdx.app.type == Application.ApplicationType.Android &&
            BaseGame.isGPS &&
            BaseGame.gps != null
        )
            BaseGame.gps!!.signIn()
    }

    private fun disposeAndSetActiveScreen() {
        blackOverlay.addAction(Actions.after(Actions.run {
            dispose()
            BaseGame.setActiveScreen(MenuScreen())
        }))
    }
}
