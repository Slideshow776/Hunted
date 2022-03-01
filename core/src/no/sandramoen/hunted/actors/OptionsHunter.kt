package no.sandramoen.hunted.actors

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class OptionsHunter(s: Stage): BaseActor(0f, 0f, s) {

    init {
        loadImage("hunter/idle")
        setRandomPosition()
        color = BaseGame.lightYellowBrown
    }

    private fun setRandomPosition() {
        val duration = MathUtils.random(1f, 1.5f)
        when (MathUtils.random(0, 2)) {
            0 -> {
                centerAtPosition(00f, -40f)
                setSize(40f, 40f)
                addAction(Actions.moveTo(00f, -20f, duration, Interpolation.circleOut))
            }
            1 -> {
                centerAtPosition(130f, 50f)
                setSize(10f, 10f)
                rotation = 45f
                addAction(Actions.moveTo(100f, 50f, duration, Interpolation.circleOut))
            }
            2 -> {
                centerAtPosition(40f, 125f)
                setSize(20f, 10f)
                rotation = 180f
                addAction(Actions.moveTo(40f, 105f, duration, Interpolation.circleOut))
            }
        }
    }
}