package no.sandramoen.hunted.actors

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class Vignette(stage: Stage) : BaseActor(0f, 0f, stage){
    init {
        loadImage("vignette")
        setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT)
        touchable = Touchable.disabled
    }
}