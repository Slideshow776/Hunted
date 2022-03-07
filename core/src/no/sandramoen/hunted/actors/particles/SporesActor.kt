package no.sandramoen.hunted.actors.particles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class SporesActor(
    private val layerNumber: Int,
    x: Float,
    y: Float,
    stage: Stage,
    private val parent: BaseActor
) : BaseActor(x, y, stage) {
    init {
        setSize(1f, 1f * BaseGame.RATIO)
        delayedStartOfEffect()
        /*debug = true*/
    }

    private fun delayedStartOfEffect() {
        val frequency = MathUtils.random(15f, 90f)
        val startTime = MathUtils.random(0f, 90f)
        println("start: $startTime, frequency: $frequency")
        addAction(Actions.sequence(
            Actions.delay(startTime),
            Actions.run {
                addAction(Actions.forever(Actions.sequence(
                    Actions.run { startEffect() },
                    Actions.delay(frequency)
                )))
            }
        ))
    }

    private fun startEffect() {
        var effect: ParticleActor? = null
        when (layerNumber) {
            1 -> effect = Layer5SporesEffect()
            2 -> effect = Layer4SporesEffect()
            3 -> effect = Layer3SporesEffect()
            4 -> effect = Layer2SporesEffect()
            else -> {
                Gdx.app.error(
                    javaClass.simpleName,
                    "Error, could not add spores, layer is: $layerNumber"
                )
            }
        }

        if (effect != null) {
            effect.setScale(.0008f)
            effect.centerAtActor(this)
            parent.addActor(effect)
            effect.start()
        }
    }
}