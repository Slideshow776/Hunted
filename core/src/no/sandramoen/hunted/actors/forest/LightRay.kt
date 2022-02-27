package no.sandramoen.hunted.actors.forest

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import no.sandramoen.hunted.utils.BaseActor

class LightRay(x: Float, y: Float, stage: Stage, rotation: Float) : BaseActor(x, y, stage) {
    init {
        loadImage("lightRay")
        setSize(1f, 100f)
        setPosition(x, y)
        setOrigin(Align.center)
        touchable = Touchable.disabled
        color.a = .0f
        rotateBy(rotation)
        open()
    }

    private fun open() {
        addAction(
            Actions.sequence(
                Actions.delay(MathUtils.random(5f, 180f)),
                Actions.parallel(
                    Actions.alpha(.125f, 10f),
                    Actions.scaleTo(width * MathUtils.random(10f, 20f), 1f, MathUtils.random(5f, 15f))
                ),
                Actions.run { close() }
            )
        )
    }

    private fun close() {
        addAction(
            Actions.sequence(
                Actions.delay(MathUtils.random(0f, 60f)),
                Actions.parallel(
                    Actions.alpha(.125f, 10f),
                    Actions.scaleTo(0f, 1f, MathUtils.random(5f, 15f))
                ),
                Actions.run {
                    x = MathUtils.random(10f, 90f)
                    open()
                }
            )
        )
    }
}
