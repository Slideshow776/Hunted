package no.sandramoen.hunted.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class Leaf(x: Float, y: Float, s: Stage, color: Color, layer: Int) : BaseActor(x, y, s) {
    init {
        loadImage("leaf")
        touchable = Touchable.disabled

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

        move()
        rotate()
    }

    override fun act(dt: Float) {
        super.act(dt)
        if (y + height < 0)
            respawn()
    }

    private fun move() {
        val amountX = MathUtils.random(.1f, 1.2f)
        val amountY = MathUtils.random(.75f, 1.5f)
        val duration = MathUtils.random(8f, 12f)
        val direction = if (MathUtils.randomBoolean()) -1f else 1f
        addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.moveBy(direction * amountX, -2 * amountY, duration),
                    Actions.moveBy(2 * amountX * -direction, -4 * amountY, 2 * duration),
                    Actions.moveBy(direction * amountX, -2 * amountY, duration)
                )
            )
        )
    }

    private fun rotate() {
        val duration = 4f
        val rotationAmount = MathUtils.random(10f, 20f)
        addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.rotateBy(rotationAmount, duration),
                    Actions.rotateBy(-2 * rotationAmount, duration),
                    Actions.rotateBy(rotationAmount, duration)
                )
            )
        )
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
