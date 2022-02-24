package no.sandramoen.hunted.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class Leaf(x: Float, y: Float, s: Stage, color: Color, layer: Int) : BaseActor(x, y, s) {
    private val tempo = MathUtils.random(.005f, .01f)

    init {
        loadImage("leaf")

        val scale = 1f
        when (layer) {
            1 -> setSize(.25f * scale, .25f * BaseGame.RATIO * scale)
            2 -> setSize(.2f * scale, .2f * BaseGame.RATIO * scale)
            3 -> setSize(.15f * scale, .15f * BaseGame.RATIO * scale)
            4 -> setSize(.1f * scale, .1f * BaseGame.RATIO * scale)
        }
        setOrigin(Align.center)
        centerAtPosition(x, y)
        setRandomPosition()
        this.color = color
    }

    override fun act(dt: Float) {
        super.act(dt)
        y -= tempo
        if (y + height < 0)
            respawn()
    }

    private fun setRandomPosition() {
        setPosition(
            MathUtils.random(0f, 100f),
            MathUtils.random(0f, 100f)
        )
    }

    private fun respawn() {
        setPosition(
            MathUtils.random(0f, 100f),
            100f
        )
    }
}
