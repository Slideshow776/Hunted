package no.sandramoen.hunted.actors

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.utils.Align
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class Net(x: Float, y: Float, s: Stage) : BaseActor(x, y, s) {
    var isShot = false

    init {
        loadImage("net")
        setSize(1f, 1f)
        setOrigin(Align.center)
        color.a = 0f
    }

    fun shoot(x: Float, y: Float, travelAmount: Float) {
        isShot = true
        centerAtPosition(x, y)
        color.a = 1f
        addAction(
            Actions.sequence(
                move(travelAmount),
                playSounds(travelAmount),
                Actions.delay(.125f),
                playerStruggle()
            )
        )
    }

    fun reset() {
        isShot = false
        actions.clear()
        setScale(1f, 1f)
        color.a = 0f
    }

    private fun playSounds(travelAmount: Float): SequenceAction {
        return Actions.sequence(
            Actions.delay(.05f),
            Actions.run { BaseGame.netCaughtSound!!.play(BaseGame.soundVolume) },
            Actions.delay(travelAmount * .4f),
            Actions.run { BaseGame.netFireSound!!.play(BaseGame.soundVolume) }
        )
    }

    private fun playerStruggle(): RepeatAction? {
        return Actions.forever(
            Actions.sequence(
                Actions.moveBy(1f, 1f, .25f),
                Actions.moveBy(-1f, -1f, .25f)
            )
        )
    }

    private fun move(travelAmount: Float): ParallelAction {
        return Actions.parallel(
            Actions.rotateBy(360f * MathUtils.random(2, 5), travelAmount),
            Actions.moveTo(50f, 50f, travelAmount),
            Actions.scaleTo(200f, 200f, travelAmount, Interpolation.exp10In)
        )
    }
}
