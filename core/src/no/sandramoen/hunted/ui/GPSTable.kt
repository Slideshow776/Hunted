package no.sandramoen.hunted.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.GameUtils

class GPSTable(val achievementButton: AchievementButton, val leaderboardsButton: LeaderboardsButton): Table() {
    private var onImage: Image
    private var offImage: Image
    private var toggleGPS: Button

    private var up: TextureRegion
    private var down: TextureRegion

    private var attemptedToSignIn = false

    init {
        val gpsLabel = Label("Google Play Services", BaseGame.labelStyle)
        gpsLabel.setFontScale(.5f)

        onImage = Image(BaseGame.textureAtlas!!.findRegion("gpsOn"))
        offImage = Image(BaseGame.textureAtlas!!.findRegion("gpsOff"))

        up = BaseGame.textureAtlas!!.findRegion("on")
        down = BaseGame.textureAtlas!!.findRegion("off")
        val buttonStyle = Button.ButtonStyle()
        buttonStyle.up = TextureRegionDrawable(up)
        buttonStyle.checked = TextureRegionDrawable(down)
        toggleGPS = Button(buttonStyle)
        toggleGPS.isChecked = BaseGame.gps != null && !BaseGame.gps!!.isSignedIn()
        toggleGPS.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                BaseGame.clickSound!!.play(BaseGame.soundVolume)
                if (!BaseGame.isGPS) {
                    if (BaseGame.gps != null)
                        BaseGame.gps!!.signIn()
                    attemptedToSignIn = true
                } else {
                    BaseGame.isGPS = false
                    if (BaseGame.gps != null && BaseGame.gps!!.isSignedIn())
                        BaseGame.gps!!.signOut()
                    achievementButton.disable()
                    leaderboardsButton.disable()
                    GameUtils.saveGameState()
                    setToggleButtonColors(toggleGPS, onImage, offImage)
                    attemptedToSignIn = false
                }

                toggleGPS.style.up = TextureRegionDrawable(down)
                toggleGPS.style.checked = TextureRegionDrawable(down)
            }
        })
        setToggleButtonColors(toggleGPS, onImage, offImage)

        add(gpsLabel).colspan(3).padBottom(Gdx.graphics.height * .03f).row()
        add(offImage).width(Gdx.graphics.width * .1f).height(Gdx.graphics.height * .045f).right()
        add(toggleGPS).width(Gdx.graphics.width * .15f).height(Gdx.graphics.height * .037f)
        add(onImage).width(Gdx.graphics.width * .1f).height(Gdx.graphics.height * .045f).left()
    }

    fun update() {
        if (attemptedToSignIn && BaseGame.gps!!.isSignedIn()) {
            BaseGame.isGPS = true

            achievementButton.update()
            leaderboardsButton.update()

            GameUtils.saveGameState()
            setToggleButtonColors(toggleGPS, onImage, offImage)
            toggleGPS.style.up = TextureRegionDrawable(up)
            toggleGPS.style.checked = TextureRegionDrawable(up)
            attemptedToSignIn = false
        }
    }

    private fun setToggleButtonColors(toggleButton: Button, onImage: Image, offImage: Image) {
        if (!toggleButton.isChecked) {
            onImage.color = Color.WHITE
            offImage.color = Color.DARK_GRAY
        } else {
            onImage.color = Color.DARK_GRAY
            offImage.color = Color.WHITE
        }
    }
}