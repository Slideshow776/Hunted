package no.sandramoen.hunted.screens.gameplay

import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input.Peripheral
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.actors.ForestLayer
import no.sandramoen.hunted.utils.BaseScreen

class LevelScreen : BaseScreen() {
    private val forestLayers = Array<ForestLayer>()
    private val isAccelerometerAvailable = input.isPeripheralAvailable(Peripheral.Accelerometer)
    private val accelerometerXOffset = 6f // offsets the device to a tilted position towards the player
    private val mouseMovedDeadZone = .5f
    private val desktopLayerShuffleModifier = .5f

    private var mousePosition = Vector2(50f, 50f)

    override fun initialize() {
        for (i in 5 downTo 1)
            forestLayers.add(ForestLayer(mainStage, "forest/Layer $i"))

        val camera = mainStage.camera as OrthographicCamera
        camera.zoom -= .025f
    }

    override fun update(dt: Float) {
        for (layer in forestLayers)
            layer.act(dt)
        accelerometer()
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
