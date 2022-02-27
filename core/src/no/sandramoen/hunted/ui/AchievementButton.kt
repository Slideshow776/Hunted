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

class AchievementButton() : Table(){
    private var achievementButton: TextButton
    private var achievementImage: Image

    init {
        achievementButton = TextButton("Achievements", BaseGame.textButtonStyle)
        achievementButton.label.setFontScale(.5f)
        achievementButton.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                BaseGame.clickSound!!.play(BaseGame.soundVolume)
                achievementButton.label.color = BaseGame.lightPink
                if (BaseGame.gps != null && BaseGame.gps!!.isSignedIn())
                    BaseGame.gps!!.showAchievements()
            }
        })
        GameUtils.addTextButtonEnterExitEffect(achievementButton)

        achievementImage = Image(BaseGame.textureAtlas!!.findRegion("achievements-google-play-achievements-icon"))
        if (BaseGame.gps != null && BaseGame.gps!!.isSignedIn()) {
            achievementButton.touchable = Touchable.enabled
        } else {
            achievementImage.color = Color.DARK_GRAY
            achievementButton.touchable = Touchable.disabled
            achievementButton.label.color = Color.DARK_GRAY
        }

        add(achievementButton).padRight(Gdx.graphics.width * .025f)
        add(achievementImage).width(Gdx.graphics.width * .06f).height(Gdx.graphics.height * .045f)
    }

    fun disable() {
        achievementButton.touchable = Touchable.disabled
        achievementButton.label.color = Color.DARK_GRAY
        achievementImage.color = Color.DARK_GRAY
    }

    fun update() {
        achievementButton.touchable = Touchable.enabled
        achievementButton.label.color = Color.WHITE
        achievementImage.color = Color.WHITE
    }
}