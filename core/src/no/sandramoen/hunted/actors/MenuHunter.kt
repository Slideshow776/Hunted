package no.sandramoen.hunted.actors

import com.badlogic.gdx.scenes.scene2d.Stage
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class MenuHunter(x: Float, y: Float, s: Stage) : BaseActor(x, y, s) {
    init {
        loadImage("hunter/idle")
        setSize(10f, 10f)
        color = BaseGame.lightYellowBrown
    }
}