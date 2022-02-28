package no.sandramoen.hunted.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class MenuShot(x: Float, y: Float, s: Stage) : BaseActor(x, y, s) {
    private val warmBrown = Color(0.941f, 0.624f, 0.443f, 1f)
    private val lightWineRed = Color(0.663f, 0.294f, 0.329f, 1f)

    init {
        loadImage("shot")
        setSize(2f, 2f)
        color = lightWineRed
        addAction(Actions.parallel(
            Actions.color(warmBrown, .5f),
            Actions.moveBy(120f, 0f, 1f)
        ))
        BaseGame.shotSound!!.play(BaseGame.soundVolume)
    }
}