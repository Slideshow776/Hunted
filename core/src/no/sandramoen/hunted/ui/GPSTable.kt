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

class GPSTable(): Table() {
    private lateinit var onImage: Image
    private lateinit var offImage: Image
    private lateinit var toggleGPS: Button

    private lateinit var up: TextureRegion
    private lateinit var down: TextureRegion

    private var attemptedToSignIn = false

    private val achievementButton = TextButtonWithImage("Achievements", "achievements-icon")
    private val leaderboardsButton = TextButtonWithImage("Leaderboards", "leaderboards-icon")

    init {
        val gpsLabel = Label("Google Play Services", BaseGame.smallLabelStyle)
        gpsLabel.setFontScale(1.25f)

        add(gpsLabel).colspan(3).padBottom(Gdx.graphics.height * .02f).row()
        add(toggleButton()).row()
        add(achievementButton).padTop(Gdx.graphics.height * .03f).colspan(2).row()
        add(leaderboardsButton).padTop(Gdx.graphics.height * .03f).colspan(2).row()

        /*debug = true*/
    }

    fun update() {
        if (attemptedToSignIn && BaseGame.gps!!.isSignedIn()) {
            BaseGame.isGPS = true

            achievementButton.enable()
            leaderboardsButton.enable()

            GameUtils.saveGameState()
            setToggleButtonColors(toggleGPS, onImage, offImage)
            toggleGPS.style.up = TextureRegionDrawable(up)
            toggleGPS.style.checked = TextureRegionDrawable(up)
            attemptedToSignIn = false
        }
    }

    private fun toggleButton(): Table {
        onImage = Image(BaseGame.textureAtlas!!.findRegion("gpsOn"))
        offImage = Image(BaseGame.textureAtlas!!.findRegion("gpsOff"))

        up = BaseGame.textureAtlas!!.findRegion("on")
        down = BaseGame.textureAtlas!!.findRegion("off")
        val buttonStyle = Button.ButtonStyle()
        buttonStyle.up = TextureRegionDrawable(up)
        buttonStyle.checked = TextureRegionDrawable(down)
        toggleGPS = Button(buttonStyle)
        toggleGPS.isChecked = BaseGame.gps != null && !BaseGame.gps!!.isSignedIn()
        setToggleListener()
        setToggleButtonColors(toggleGPS, onImage, offImage)

        val table = Table()
        table.add(offImage).width(Gdx.graphics.width * .07f).height(Gdx.graphics.height * .1f).padRight(Gdx.graphics.width * .02f)
        table.add(toggleGPS).width(Gdx.graphics.width * .08f).height(Gdx.graphics.height * .07f).padRight(Gdx.graphics.width * .02f)
        table.add(onImage).width(Gdx.graphics.width * .07f).height(Gdx.graphics.height * .1f)
        /*table.debug = true*/
        return table
    }

    private fun setToggleListener() {
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