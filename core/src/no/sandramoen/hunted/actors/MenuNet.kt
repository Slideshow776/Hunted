package no.sandramoen.hunted.actors

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class MenuNet(x: Float, y: Float, s: Stage) : BaseActor(x, y, s) {
    init {
        loadImage("net")
        setSize(20f, 10f)

        BaseGame.netFireSound!!.play(BaseGame.soundVolume)
        addAction(Actions.sequence(
            /*Actions.delay(.25f),*/
            Actions.moveTo(x + 10f, 30f, .25f),
            Actions.run { BaseGame.netCaughtSound!!.play(BaseGame.soundVolume) }
        ))
    }
}
