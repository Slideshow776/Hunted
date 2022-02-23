package no.sandramoen.hunted.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input.Peripheral
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.actors.ForestLayer
import no.sandramoen.hunted.actors.Hunter
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.BaseScreen
import no.sandramoen.hunted.utils.StoryEngine

class LevelScreen : BaseScreen() {
    private val forestLayers = Array<ForestLayer>()
    private val isAccelerometerAvailable = input.isPeripheralAvailable(Peripheral.Accelerometer)
    private val accelerometerXOffset = 6f // offsets the device to a tilted position towards the player
    private val mouseMovedDeadZone = .5f
    private val desktopLayerShuffleModifier = .5f

    private lateinit var hunter: Hunter
    private var mousePosition = Vector2(50f, 50f)

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

        for (layer in forestLayers)
            layer.touchable = Touchable.disabled

        hunter = Hunter(mainStage, forestLayers)

        val camera = mainStage.camera as OrthographicCamera
        camera.zoom -= .025f

        timerLabel.color = Color(0.859f, 0.788f, 0.706f, 1f)
        storyLabel.color = Color(0.988f, 0.925f, 0.82f, 1f)
        val padding = Gdx.graphics.height * .01f
        uiTable.add(timerLabel).expandY().top().padTop(padding).row()
        uiTable.add(storyLabel).bottom().padBottom(padding)
        /*uiTable.debug = true*/
    }

    override fun update(dt: Float) {
        for (i in 0 until forestLayers.size)
            forestLayers[i].act(dt)
        accelerometer()
        updateTimer(dt)
        storyEngine.update(dt, timer)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val worldCoordinates = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))

        if ((worldCoordinates.x - mousePosition.x) < -mouseMovedDeadZone && forestLayers.last().x <= 1f) {
            shuffleLayersRight(desktopLayerShuffleModifier)
        } else if ((worldCoordinates.x - mousePosition.x) > mouseMovedDeadZone && forestLayers.last().x >= -1f) {
            shuffleLayersLeft(desktopLayerShuffleModifier)
        }

        if ((worldCoordinates.y - mousePosition.y) < -mouseMovedDeadZone && forestLayers.last().y <= 1f) {
            shuffleLayersUp(desktopLayerShuffleModifier)
        } else if ((worldCoordinates.y - mousePosition.y) > mouseMovedDeadZone && forestLayers.last().y >= -1f) {
            shuffleLayersDown(desktopLayerShuffleModifier)
        }
        mousePosition = Vector2(worldCoordinates.x, worldCoordinates.y)
        return super.mouseMoved(screenX, screenY)
    }

    private fun updateTimer(dt: Float) {
        if (hunter.hidden && timer > 0f) {
            timer -= dt
            timerLabel.setText("${timer.toInt()}")
            if (timerLabel.color.a == 0f)
                timerLabel.addAction(Actions.fadeIn(.5f))
        } else if (timer <= 0f) {
            timer = timerStartValue
            hunter.revealHunter()
            timerLabel.addAction(Actions.sequence(
                Actions.delay(2f), // hardcoded to fit with hunter reset
                Actions.fadeOut(.5f)
            ))
        }
    }

    private fun accelerometer() {
        if (isAccelerometerAvailable) {
            if (input.accelerometerY > 1f && forestLayers.last().x <= 1f)
                shuffleLayersRight()
            else if (input.accelerometerY < -1f && forestLayers.last().x >= -1f)
                shuffleLayersLeft()

            if (input.accelerometerX > 1f + accelerometerXOffset && forestLayers.last().y <= 1f)
                shuffleLayersUp()
            else if (input.accelerometerX < -1f + accelerometerXOffset && forestLayers.last().y >= -1f)
                shuffleLayersDown()
        }
    }

    private fun shuffleLayersRight(modifier: Float = 1f) {
        var shuffleLength = .00125f * modifier
        for (layer in forestLayers) {
            layer.x += shuffleLength
            shuffleLength *= 2
        }
    }

    private fun shuffleLayersLeft(modifier: Float = 1f) {
        var shuffleLength = .00125f * modifier
        for (layer in forestLayers) {
            layer.x -= shuffleLength
            shuffleLength *= 2
        }
    }

    private fun shuffleLayersUp(modifier: Float = 1f) {
        var shuffleLength = .00125f * modifier
        for (layer in forestLayers) {
            layer.y += shuffleLength
            shuffleLength *= 2
        }
    }

    private fun shuffleLayersDown(modifier: Float = 1f) {
        var shuffleLength = .00125f * modifier
        for (layer in forestLayers) {
            layer.y -= shuffleLength
            shuffleLength *= 2
        }
    }
}
