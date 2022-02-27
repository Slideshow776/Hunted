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

class Bird(x: Float, y: Float, s: Stage, color: Color, layer: Int) : BaseActor(x, y, s) {
    private var movementSpeed = -1f

    init {
        var animationImages: Array<TextureAtlas.AtlasRegion> = Array()
        animationImages.add(BaseGame.textureAtlas!!.findRegion("bird/flying1"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("bird/flying2"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("bird/flying3"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("bird/flying4"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("bird/flying5"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("bird/flying6"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("bird/flying7"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("bird/flying8"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("bird/flying9"))
        val flyingAnimation = Animation(MathUtils.random(.1f,  .15f), animationImages, Animation.PlayMode.LOOP)
        animationImages.clear()

        setAnimation(flyingAnimation)

        val scale = 2f
        when (layer) {
            1 -> {
                setSize(.25f * scale, .25f * BaseGame.RATIO * scale)
                movementSpeed = .01f
            }
            2 -> {
                setSize(.2f * scale, .2f * BaseGame.RATIO * scale)
                movementSpeed = .008f
            }
            3 -> {
                setSize(.15f * scale, .15f * BaseGame.RATIO * scale)
                movementSpeed = .006f
            }
            4 -> {
                setSize(.1f * scale, .1f * BaseGame.RATIO * scale)
                movementSpeed = .004f
            }
        }
        setOrigin(Align.center)
        touchable = Touchable.disabled
        this.color = color
        setPosition(MathUtils.random(-100f, 200f), MathUtils.random(40f, 90f))

        /*debug = true*/
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
            x = MathUtils.random(-100f, 0f)
            y = MathUtils.random(40f, 90f)
        } else {
            isFacingRight = false
            x = MathUtils.random(100f, 200f)
            y = MathUtils.random(40f, 90f)
        }

        addAction(Actions.sequence(
            Actions.delay(MathUtils.random(0f, 180f)),
            Actions.moveBy(0f, MathUtils.random(-15f, 15f), 10f)
        ))
    }
}
