package no.sandramoen.hunted.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.utils.Align
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.GameUtils

class Shot(x: Float, y: Float, s: Stage, layer: Int) : BaseActor(x, y, s) {
    private val layer = layer
    private val warmBrown = Color(0.941f, 0.624f, 0.443f, 1f)
    private val lightWineRed = Color(0.663f, 0.294f, 0.329f, 1f)

    init {
        loadImage("shot")
        setSize(.2f, .2f)
        color = warmBrown
        setOrigin(Align.center)

        animateWithSounds()
    }

    private fun animateWithSounds() {
        addAction(
            Actions.sequence(
                movement(),
                sounds()
            )
        )
    }

    private fun movement(): SequenceAction? {
        return Actions.sequence(
            Actions.parallel(
                Actions.rotateBy(360f, GameUtils.shotTravelAmount(layer)),
                Actions.color(lightWineRed, GameUtils.shotTravelAmount(layer), Interpolation.pow5Out),
                Actions.scaleBy(200f, 200f, GameUtils.shotTravelAmount(layer), Interpolation.pow5In),
                Actions.moveTo(shotMissAmount().x, shotMissAmount().y, GameUtils.shotTravelAmount(layer), Interpolation.exp10In)
            ),
            Actions.run { isVisible = false }
        )
    }

    private fun shotMissAmount(): Vector2 {
        val x = if (MathUtils.randomBoolean()) MathUtils.random(-40f, -20f) else MathUtils.random(20f, 40f)
        val y = if (MathUtils.randomBoolean()) MathUtils.random(-40f, -20f) else MathUtils.random(20f, 40f)
        return Vector2(50f + x, 50f + y)
    }

    private fun sounds(): SequenceAction {
        return Actions.sequence(
            Actions.delay(.05f),
            Actions.run { BaseGame.swooshSound!!.play(BaseGame.soundVolume) },
            Actions.delay(GameUtils.shotTravelAmount(layer) * .4f),
            Actions.run {
                BaseGame.shotSound!!.play(BaseGame.soundVolume)
                remove()
            }
        )
    }
}
