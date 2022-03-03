package no.sandramoen.hunted.actors.forest

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class Moose(s: Stage, color: Color, val layer: Int) : BaseActor(0f, 0f, s) {
    private var movementSpeed = -1f
    private var modifier = 1.25f

    init {
        var animationImages: Array<TextureAtlas.AtlasRegion> = Array()
        animationImages.add(BaseGame.textureAtlas!!.findRegion("moose/walking1"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("moose/walking2"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("moose/walking3"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("moose/walking4"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("moose/walking5"))
        val walkingAnimation = Animation(.75f, animationImages, Animation.PlayMode.LOOP)
        animationImages.clear()

        setAnimation(walkingAnimation)

        val scale = 15f
        when (layer) {
            1 -> {
                setSize(.25f * scale, .25f * BaseGame.RATIO * scale)
                movementSpeed = .01f * .75f
            }
            2 -> {
                setSize(.2f * scale, .2f * BaseGame.RATIO * scale)
                movementSpeed = .008f * .75f
            }
            3 -> {
                setSize(.15f * scale, .15f * BaseGame.RATIO * scale)
                movementSpeed = .006f * .75f
            }
            4 -> {
                setSize(.1f * scale, .1f * BaseGame.RATIO * scale)
                movementSpeed = .004f * .75f
            }
        }
        setOrigin(Align.center)
        touchable = Touchable.disabled
        /*this.color = Color.PINK*/
        this.color = color
        if (layer == 4) modifier = 2f
        setPosition(MathUtils.random(-50f, 150f), 2f + layer * modifier)
        debug = true
    }

    override fun act(dt: Float) {
        super.act(dt)

        if (isFacingRight) {
            x += movementSpeed
            if (x > 100f) {
                randomPosition()
            }
        } else {
            x -= movementSpeed
            if (x + width < 0f) {
                randomPosition()
            }
        }
    }

    private fun randomPosition() {
        if (MathUtils.randomBoolean()) {
            isFacingRight = true
            x = MathUtils.random(-50f, 0f)
            y = 2f + layer * modifier
        } else {
            isFacingRight = false
            x = MathUtils.random(100f, 150f)
            y = 2f + layer * modifier
        }
    }
}
