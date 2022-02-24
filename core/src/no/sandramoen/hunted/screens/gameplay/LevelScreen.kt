package no.sandramoen.hunted.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.actors.ForestLayer
import no.sandramoen.hunted.actors.Hunter
import no.sandramoen.hunted.actors.Net
import no.sandramoen.hunted.actors.Vignette
import no.sandramoen.hunted.utils.*

class LevelScreen : BaseScreen() {
    private val forestLayers = Array<ForestLayer>()
    private lateinit var io: IO

    private lateinit var hunter: Hunter
    private lateinit var net: Net

    private val timerStartValue = 61f
    private var timer = timerStartValue
    private var timerLabel = Label("$timer", BaseGame.labelStyle)

    private var storyLabel = Label("LevelScreen", BaseGame.labelStyle)
    private var storyEngine = StoryEngine(storyLabel, timer)

    override fun initialize() {
        forestLayers.add(ForestLayer(mainStage, "forest/Layer 5", Color(0.627f, 0.867f, 0.827f, 1f)))
        forestLayers.add(ForestLayer(mainStage, "forest/Layer 4", Color(0.435f, 0.69f, 0.718f, 1f)))
        forestLayers.add(ForestLayer(mainStage, "forest/Layer 3", Color(0.341f, 0.498f, 0.616f, 1f)))
        forestLayers.add(ForestLayer(mainStage, "forest/Layer 2", Color(0.29f, 0.341f, 0.525f, 1f)))
        forestLayers.add(ForestLayer(mainStage, "forest/Layer 1", Color(0.243f, 0.231f, 0.4f, 1f)))

        io = IO(forestLayers)

        for (layer in forestLayers)
            layer.touchable = Touchable.disabled

        hunter = Hunter(mainStage, forestLayers)
        net = Net(hunter.x, hunter.y, mainStage)

        Vignette(mainStage)

        val camera = mainStage.camera as OrthographicCamera
        camera.zoom -= .025f

        timerLabel.color = Color(0.859f, 0.788f, 0.706f, 1f)
        storyLabel.color = Color(0.988f, 0.925f, 0.82f, 1f)
        val padding = Gdx.graphics.height * .01f
        uiTable.add(timerLabel).expandY().top().padTop(padding).row()
        uiTable.add(storyLabel).bottom().padBottom(padding)
        /*uiTable.debug = true*/

        GameUtils.playRandomAmbientMusic()
        GameUtils.playRandomLevelMusic()
    }

    override fun update(dt: Float) {
        for (i in 0 until forestLayers.size)
            forestLayers[i].act(dt)
        io.accelerometer()
        updateTimer(dt)
        storyEngine.update(dt, timer)
        if (!hunter.isHidden)
            restartTimer()
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        io.mouseMoved(camera, screenX, screenY)
        return super.mouseMoved(screenX, screenY)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.R) reset()
        if (keycode == Input.Keys.T) hunter.blowHorn()
        return super.keyDown(keycode)
    }

    private fun reset() {
        timer = timerStartValue
        net.reset()
        hunter.reset()
        GameUtils.stopAllMusic()
        GameUtils.playRandomAmbientMusic()
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
        }

        if (timer.toInt() == 15 && hunter.isNotBlowingHorn) {
            hunter.blowHorn()
            storyEngine.triggerHornSound()
        }
    }

    private fun restartTimer() {
        if (!timerLabel.hasActions()) {
            storyEngine.triggerFound()
            GameUtils.stopAmbientMusic()
            timer = timerStartValue
            val duration = .125f
            timerLabel.addAction(
                Actions.sequence(
                    Actions.delay(1.5f),
                    Actions.fadeOut(duration),
                    Actions.delay(1.5f),
                    Actions.fadeIn(duration)
                )
            )
        }
    }
}
