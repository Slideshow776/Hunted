package no.sandramoen.hunted.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.GameUtils

class TextButtonWithImage(buttonText: String, imagePath: String) : Table() {
    private var textButton: TextButton
    private var image: Image

    init {
        textButton = TextButton(buttonText, BaseGame.textButtonStyle)
        textButton.label.setFontScale(.65f)
        textButton.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                BaseGame.clickSound!!.play(BaseGame.soundVolume)
                textButton.label.color = BaseGame.lightPink
                if (BaseGame.gps != null && BaseGame.gps!!.isSignedIn())
                    BaseGame.gps!!.showAchievements()
            }
        })
        GameUtils.addTextButtonEnterExitEffect(textButton)

        image = Image(BaseGame.textureAtlas!!.findRegion(imagePath))
        if (BaseGame.gps != null && BaseGame.gps!!.isSignedIn()) {
            enable()
        } else {
            disable()
        }

        add(textButton).padRight(Gdx.graphics.width * .02f)
        add(image).width(Gdx.graphics.width * .045f).height(Gdx.graphics.height * .075f)
    }

    fun enable() {
        textButton.touchable = Touchable.enabled
        textButton.label.color = Color.WHITE
        image.color = Color.WHITE
    }

    fun disable() {
        textButton.touchable = Touchable.disabled
        textButton.label.color = Color.DARK_GRAY
        image.color = Color.DARK_GRAY
    }
}
