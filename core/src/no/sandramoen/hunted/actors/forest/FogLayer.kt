package no.sandramoen.hunted.actors.forest

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import no.sandramoen.hunted.actors.WaveActor
import no.sandramoen.hunted.utils.BaseActor

class FogLayer(x: Float, y: Float, s: Stage, path: String) : BaseActor(x, y, s) {
    private val layerSpeed = MathUtils.random(.0025f, .005f)
    private var fog1: WaveActor = WaveActor(x, y, s, path)
    private var fog2 = WaveActor(x + fog1.width, fog1.y, s, path)

    init {
        fog1.color.a = .3f
        fog1.amplitudeX = 0f
        fog1.amplitudeY = .1f
        fog1.waveLengthY = 5f
        fog1.velocityY = 1f
        addActor(fog1)

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

    override fun clearActions() {
        fog1.clearActions()
        fog2.clearActions()
    }

    fun delayedFadeIn(duration: Float) {
        fog1.addAction(Actions.sequence(
            Actions.delay(duration),
            Actions.parallel(
                Actions.alpha(.3f, 10f)
            )
        ))
        fog2.addAction(Actions.sequence(
            Actions.delay(duration),
            Actions.parallel(
                Actions.alpha(.3f, 10f)
            )
        ))
    }

    fun delayedFadeOut(duration: Float) {
        clearActions()
        fog1.addAction(Actions.alpha(0f, duration))
        fog2.addAction(Actions.alpha(0f, duration))
    }

    fun makeInvisible() {
        fog1.color.a = 0f
        fog2.color.a = 0f
    }

    private fun moveAndLoopImage(actor: BaseActor) {
        actor.x -= layerSpeed
        if (actor.x + actor.width <= 0)
            actor.x = actor.width
    }
}
