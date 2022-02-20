package no.sandramoen.hunted.actors

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import no.sandramoen.hunted.utils.BaseActor

class FogLayer(x: Float, y: Float, s: Stage, path: String) : BaseActor(x, y, s) {
    private val layerSpeed = MathUtils.random(.0025f, .005f)

    init {
        val fog1 = WaveActor(x, y, s, path)
        fog1.color.a = .3f
        fog1.amplitudeX = 0f
        fog1.amplitudeY = .1f
        fog1.waveLengthY = 5f
        fog1.velocityY = 1f
        addActor(fog1)

        val fog2 = WaveActor(x + fog1.width, fog1.y, s, path)
        fog2.color.a = fog1.color.a
        fog2.amplitudeX = fog1.amplitudeX
        fog2.waveLengthX = fog1.waveLengthX
        fog2.velocityX = fog1.velocityX
        fog2.amplitudeY = fog1.amplitudeY
        fog2.waveLengthY = fog1.waveLengthY
        fog2.velocityY = fog1.velocityY
        addActor(fog2)
    }

    override fun act(dt: Float) {
        for (actor in children) {
            actor.act(dt)
            moveAndLoopImage(actor as BaseActor)
        }
    }

    private fun moveAndLoopImage(actor: BaseActor) {
        actor.x -= layerSpeed
        if (actor.x + actor.width <= 0)
            actor.x = actor.width
    }
}
