package no.sandramoen.hunted.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.actors.*
import no.sandramoen.hunted.actors.forest.ForestLayer
import no.sandramoen.hunted.actors.hunter.Hunter
import no.sandramoen.hunted.actors.particles.SporesActor
import no.sandramoen.hunted.actors.particles.Layer5SporesEffect
import no.sandramoen.hunted.screens.shell.MenuScreen
import no.sandramoen.hunted.utils.*

open class LevelScreen(private val tutorial: Boolean = false) : BaseScreen() {
    private val forestLayers = Array<ForestLayer>()
    private lateinit var io: IO

    private lateinit var hunter: Hunter
    private lateinit var net: Net

    private val timerStartValue = 98f
    private var timer = timerStartValue
    private var timerLabel = Label("$timer", BaseGame.smallLabelStyle)
    private lateinit var tutorialLabel: Label

    private var storyLabel = Label("LevelScreen", BaseGame.smallLabelStyle)
    private var storyEngine : StoryEngine = StoryEngine(storyLabel, tutorial, timerStartValue.toInt(), mainStage)

    private var gameOver = false

    private var levelNumber = MathUtils.random(1, 6)

    override fun initialize() {
        val lightRayRotation = MathUtils.random(-15f, 15f)
        forestLayers.add(ForestLayer(mainStage, levelNumber, 5, Color(0.627f, 0.867f, 0.827f, 1f), lightRayRotation))
        forestLayers.add(ForestLayer(mainStage, levelNumber, 4, Color(0.435f, 0.69f, 0.718f, 1f), lightRayRotation))
        forestLayers.add(ForestLayer(mainStage, levelNumber, 3, Color(0.341f, 0.498f, 0.616f, 1f), lightRayRotation))
        forestLayers.add(ForestLayer(mainStage, levelNumber, 2, Color(0.29f, 0.341f, 0.525f, 1f), lightRayRotation))
        forestLayers.add(ForestLayer(mainStage, levelNumber, 1, Color(0.243f, 0.231f, 0.4f, 1f), lightRayRotation))

        io = IO(forestLayers)
        cinematicOpening()

        hunter = Hunter(mainStage, forestLayers, levelNumber)
        net = Net(hunter.x, hunter.y, mainStage)
        Vignette(mainStage)

        val camera = mainStage.camera as OrthographicCamera
        camera.zoom -= .025f

        timerLabel.color = BaseGame.lightBrown
        timerLabel.setFontScale(1.1f)
        if (tutorial)
            timerLabel.addAction(Actions.fadeOut(0f))
        else
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
        if (tutorial)
            tutorial()
    }

    override fun update(dt: Float) {
        for (i in 0 until forestLayers.size)
            forestLayers[i].act(dt)
        io.accelerometer()

        if (!tutorial || timer >= timerStartValue - 15f)
            updateTimer(dt)
        storyEngine.update(dt, timer.toInt())
        if (!hunter.isHidden && !gameOver)
            cinematicClosing(false)

        // Gdx.app.error(javaClass.simpleName, "FPS => ${Gdx.graphics.framesPerSecond}")
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        // println(camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f)))
        return super.touchDown(screenX, screenY, pointer, button)
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
        if (keycode == Keys.NUM_1) storyEngine.storyTrigger()
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
            BaseGame.clickSound!!.play(BaseGame.soundVolume)
            GameUtils.stopAllMusic()
            BaseGame.setActiveScreen(MenuScreen())
            storyEngine.stopAllSounds()
        }
        return super.keyDown(keycode)
    }

    private fun tutorial() {
        val stopwatch = BaseActor(0f, 0f, mainStage)
        tutorialLabel = Label(BaseGame.myBundle!!.get("tutorialText0"), BaseGame.smallLabelStyle)
        tutorialLabel.color = BaseGame.lightBrown
        tutorialLabel.addAction(Actions.fadeOut(0f))
        val group = Group()
        group.addActor(tutorialLabel)
        group.setScale(.05f)

        stopwatch.setSize(1f, 1f)
        var xPosition =
            if (hunter.clickBox.x <= 50f)
                hunter.clickBox.x + hunter.clickBox.width
            else
                hunter.clickBox.x - hunter.clickBox.width * 7
        val yPos =
            if (hunter.clickBox.y <= 50f)
                hunter.clickBox.y + hunter.clickBox.height
            else
                hunter.clickBox.y - hunter.clickBox.height
        stopwatch.setPosition(xPosition, yPos)
        stopwatch.addActor(group)
        stopwatch.addAction(Actions.sequence(
            Actions.delay(15f),
            Actions.run { hunter.clickBox.debug = true },
            Actions.delay(1f),
            Actions.run { tutorialLabel.addAction(Actions.fadeIn(2f)) },
            Actions.delay(2f),
            Actions.run { GameUtils.pulseWidget(tutorialLabel) }
        ))
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
        if (tutorial) {
            hunter.clickBox.debug = false
            tutorialLabel.clearActions()
            tutorialLabel.addAction(Actions.fadeOut(2f))
            BaseGame.tutorial = false
            GameUtils.saveGameState()
        }

        BaseGame.heartBeatFasterSound!!.play(BaseGame.soundVolume * .6f)
        BaseGame.pantingFadeInSound!!.play(BaseGame.soundVolume)
        gameOver = true
        val delayDuration = 9.5f
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
                    if (caught) {
                        BaseGame.setActiveScreen(MenuScreen())
                    } else {
                        GameUtils.rewardAchievement("found")
                        setNextLevel()
                    }
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
            playerCaught()
        }

        if (timer.toInt() == (timerStartValue * .25f).toInt() && hunter.isNotBlowingHorn) {
            hunter.blowHorn()
            timerLabel.addAction(Actions.sequence(
                Actions.delay(4.2f),
                Actions.run { storyEngine.triggerHornBlown() }
            ))
        }
    }

    private fun playerCaught() {
        net.shoot(hunter.x, hunter.y, GameUtils.shotTravelAmount(hunter.layerNumber))
        hunter.clickBox.touchable = Touchable.disabled
        storyEngine.triggerCaught()
        cinematicClosing(true)
        GameUtils.rewardAchievement("caught")
    }
}
