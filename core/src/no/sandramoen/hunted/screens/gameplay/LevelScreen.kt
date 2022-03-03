package no.sandramoen.hunted.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.actors.*
import no.sandramoen.hunted.actors.forest.ForestLayer
import no.sandramoen.hunted.actors.hunter.Hunter
import no.sandramoen.hunted.screens.shell.MenuScreen
import no.sandramoen.hunted.utils.*

open class LevelScreen(private val tutorial: Boolean = false) : BaseScreen() {
    private val forestLayers = Array<ForestLayer>()
    private lateinit var io: IO

    private lateinit var hunter: Hunter
    private lateinit var net: Net

    private val timerStartValue = 78f
    private var timer = timerStartValue
    private var timerLabel = Label("$timer", BaseGame.smallLabelStyle)

    private var storyLabel = Label("LevelScreen", BaseGame.smallLabelStyle)
    private var storyEngine : StoryEngine = StoryEngine(storyLabel, tutorial, timerStartValue.toInt(), mainStage)

    private var gameOver = false

    private var levelNumber = MathUtils.random(1, 6)

    override fun initialize() {
        val lightRayRotation = MathUtils.random(-15f, 15f)
        forestLayers.add(ForestLayer(mainStage, "forest/level$levelNumber/Layer 5", Color(0.627f, 0.867f, 0.827f, 1f), lightRayRotation))
        forestLayers.add(ForestLayer(mainStage, "forest/level$levelNumber/Layer 4", Color(0.435f, 0.69f, 0.718f, 1f), lightRayRotation))
        forestLayers.add(ForestLayer(mainStage, "forest/level$levelNumber/Layer 3", Color(0.341f, 0.498f, 0.616f, 1f), lightRayRotation))
        forestLayers.add(ForestLayer(mainStage, "forest/level$levelNumber/Layer 2", Color(0.29f, 0.341f, 0.525f, 1f), lightRayRotation))
        forestLayers.add(ForestLayer(mainStage, "forest/level$levelNumber/Layer 1", Color(0.243f, 0.231f, 0.4f, 1f), lightRayRotation))

        io = IO(forestLayers)
        cinematicOpening()

        hunter = Hunter(mainStage, forestLayers, levelNumber)
        net = Net(hunter.x, hunter.y, mainStage)
        Vignette(mainStage)

        val camera = mainStage.camera as OrthographicCamera
        camera.zoom -= .025f

        timerLabel.color = BaseGame.lightBrown
        timerLabel.setFontScale(1.1f)
        timerLabel.addAction(Actions.sequence(
            Actions.delay(2f),
            Actions.fadeIn(.125f)
        ))
        storyLabel.color = BaseGame.lightBrown
        storyLabel.setFontScale(1.1f)
        uiTable.add(timerLabel).expandY().top().padTop(Gdx.graphics.height * .01f).row()
        uiTable.add(storyLabel).bottom().padBottom(Gdx.graphics.height * .01f)
        /*uiTable.debug = true*/

        GameUtils.playAmbientMusicWithRandomStart()
        GameUtils.playRandomLevelMusic()
    }

    override fun update(dt: Float) {
        for (i in 0 until forestLayers.size)
            forestLayers[i].act(dt)
        io.accelerometer()
        updateTimer(dt)
        storyEngine.update(dt, timer.toInt())
        if (!hunter.isHidden && !gameOver)
            cinematicClosing(false)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        io.mouseMoved(camera, screenX, screenY)
        return super.mouseMoved(screenX, screenY)
    }

    override fun keyDown(keycode: Int): Boolean { // debugging purposes
        if (keycode == Keys.R) reset()
        if (keycode == Keys.T) hunter.blowHorn()
        if (keycode == Keys.Q) timer = timerStartValue
        if (keycode == Keys.NUM_2) hunter.clickBox.debug = !hunter.clickBox.debug
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
            BaseGame.clickSound!!.play(BaseGame.soundVolume)
            GameUtils.stopAllMusic()
            BaseGame.setActiveScreen(MenuScreen())
        }
        return super.keyDown(keycode)
    }

    private fun cinematicOpening() {
        BaseGame.heartBeatSlowerSound!!.play(BaseGame.soundVolume * .6f)
        BaseGame.pantingFadeOutSound!!.play(BaseGame.soundVolume * .6f)
        BaseGame.bushSound!!.play(BaseGame.soundVolume, MathUtils.random(.9f, 1.1f), 0f)
        val duration = 10f
        forestLayers.last().fog.makeInvisible()
        forestLayers.last().fog.delayedFadeIn(duration)
        for (layer in forestLayers) {
            layer.addAction(Actions.moveBy(0f, -105f, duration, Interpolation.exp10Out))
        }
    }

    private fun cinematicClosing(caught: Boolean) {
        BaseGame.heartBeatFasterSound!!.play(BaseGame.soundVolume * .6f)
        BaseGame.pantingFadeInSound!!.play(BaseGame.soundVolume)
        gameOver = true
        val delayDuration = 9.5f
        forestLayers.last().fog.clearActions()
        forestLayers.last().fog.delayedFadeOut(delayDuration)
        for (layer in forestLayers) {
            layer.addAction(Actions.sequence(
                    Actions.delay(delayDuration),
                    Actions.moveBy(0f, 105f, 10f, Interpolation.exp10Out)
            ))
        }
        net.addAction(Actions.sequence(
            Actions.delay(delayDuration),
            Actions.fadeOut(2f)
        ))

        if (!net.isShot)
            storyEngine.triggerFound(hunter.detectedPlayer)

        timerLabel.addAction(
            Actions.sequence(
                Actions.delay(2f),
                Actions.run { BaseGame.bushSound!!.play(BaseGame.soundVolume, MathUtils.random(.9f, 1.1f), 0f) },
                Actions.fadeOut(.125f),
                Actions.delay(delayDuration * .6f),
                Actions.run {
                    GameUtils.stopAmbientMusic()
                    GameUtils.stopAllMusic()
                    BaseGame.heartBeatSlowerSound!!.stop()
                    BaseGame.heartBeatFasterSound!!.stop()
                    if (caught)
                        BaseGame.setActiveScreen(MenuScreen())
                    else
                        setNextLevel()
                }
            )
        )
    }

    private fun setNextLevel() {
        BaseGame.setActiveScreen(LevelScreen())
    }

    private fun reset() {
        timer = timerStartValue
        net.reset()
        hunter.reset()
        GameUtils.stopAllMusic()
        GameUtils.playAmbientMusicWithRandomStart()
        GameUtils.playRandomLevelMusic()
    }

    private fun updateTimer(dt: Float) {
        if (hunter.isHidden && timer > 0f) {
            timer -= dt
            timerLabel.setText("${timer.toInt()}")
        } else if (timer <= 0f && timer >= -10f) {
            timer = -11f
            net.shoot(hunter.x, hunter.y, GameUtils.shotTravelAmount(hunter.layerNumber))
            hunter.clickBox.touchable = Touchable.disabled
            storyEngine.triggerCaught()
            cinematicClosing(true)
        }

        if (timer.toInt() == (timerStartValue * .25f).toInt() && hunter.isNotBlowingHorn) {
            hunter.blowHorn()
            storyEngine.triggerHornSound()
        }
    }
}
