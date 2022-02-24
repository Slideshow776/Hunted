package no.sandramoen.hunted.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Peripheral
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.actors.ForestLayer

class IO(forestLayers: Array<ForestLayer>) {
    private val forestLayers = forestLayers
    private val isAccelerometerAvailable = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
    private val accelerometerXOffset = 6f // offsets device to tilted position towards the player
    private val mouseMovedDeadZone = .5f
    private val desktopLayerShuffleModifier = .5f

    private var mousePosition = Vector2(50f, 50f)

    fun mouseMoved(camera: OrthographicCamera, screenX: Int, screenY: Int) {
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
    }

    fun accelerometer() {
        if (isAccelerometerAvailable) {
            if (Gdx.input.accelerometerY > 1f && forestLayers.last().x <= 1f)
                shuffleLayersRight()
            else if (Gdx.input.accelerometerY < -1f && forestLayers.last().x >= -1f)
                shuffleLayersLeft()

            if (Gdx.input.accelerometerX > 1f + accelerometerXOffset && forestLayers.last().y <= 1f)
                shuffleLayersUp()
            else if (Gdx.input.accelerometerX < -1f + accelerometerXOffset && forestLayers.last().y >= -1f)
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
