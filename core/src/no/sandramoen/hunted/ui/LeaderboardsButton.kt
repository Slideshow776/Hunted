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

class LeaderboardsButton(): Table() {
    private var leaderboardsButton: TextButton
    private var leaderboardsImage: Image

    init {
        leaderboardsButton = TextButton("Leaderboards", BaseGame.textButtonStyle)
        leaderboardsButton.label.setFontScale(.5f)
        leaderboardsButton.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                BaseGame.clickSound!!.play(BaseGame.soundVolume)
                leaderboardsButton.label.color = BaseGame.lightPink
                if (BaseGame.gps != null && BaseGame.gps!!.isSignedIn())
                    BaseGame.gps!!.showLeaderboard()
            }
        })
        GameUtils.addTextButtonEnterExitEffect(leaderboardsButton)

        leaderboardsImage = Image(BaseGame.textureAtlas!!.findRegion("leaderboard-icon-0"))
        if (BaseGame.gps != null && BaseGame.gps!!.isSignedIn()) {
            leaderboardsButton.touchable = Touchable.enabled
        } else {
            leaderboardsImage.color = Color.DARK_GRAY
            leaderboardsButton.touchable = Touchable.disabled
            leaderboardsButton.label.color = Color.DARK_GRAY
        }

        add(leaderboardsButton).padRight(Gdx.graphics.width * .025f)
        add(leaderboardsImage).width(Gdx.graphics.width * .06f).height(Gdx.graphics.height * .045f)
    }

    fun disable() {
        leaderboardsButton.touchable = Touchable.disabled
        leaderboardsButton.label.color = Color.DARK_GRAY
        leaderboardsImage.color = Color.DARK_GRAY
    }

    fun update() {
        leaderboardsButton.touchable = Touchable.enabled
        leaderboardsButton.label.color = Color.WHITE
        leaderboardsImage.color = Color.WHITE
    }
}