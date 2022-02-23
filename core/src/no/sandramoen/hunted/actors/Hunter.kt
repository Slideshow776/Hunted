package no.sandramoen.hunted.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class Hunter(stage: Stage, forestLayers: Array<ForestLayer>) : BaseActor(0f, 0f, stage) {
    private val forestLayers = forestLayers
    private val revealScaleAmount = .5f

    private lateinit var forestLayer: ForestLayer
    private lateinit var clickBox: BaseActor
    private lateinit var idleAnimation: Animation<TextureAtlas.AtlasRegion>
    private lateinit var somersaultAnimation: Animation<TextureAtlas.AtlasRegion>
    private lateinit var currentAnimation: Animation<TextureAtlas.AtlasRegion>

    private var jump = false
    private var lightYellowBrown = Color(0.969f, 0.812f, 0.569f, 1f)

    var hidden = true
    var inAction = false
    var layerNumber: Int = -1

    init {
        animationSetUp()
        val scale = 1f
        setSize(.5f * scale, 1f * BaseGame.RATIO * scale)
        setOrigin(Align.center)

        initializeClickBox()
        setUpLayerAndPosition()

        setAcceleration(10f)
        setMaxSpeed(5f)
        setDeceleration(10f)

        // color = lightYellowBrown // debug
    }

    override fun act(dt: Float) {
        super.act(dt)
        if (jump)
            accelerateAtAngle(270f)
        applyPhysics(dt)
    }

    private fun animationSetUp() {
        var animationImages: Array<TextureAtlas.AtlasRegion> = Array()

        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/idle"))
        idleAnimation = Animation(1f, animationImages)
        animationImages.clear()

        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/somersault1"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/somersault2"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/somersault3"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/somersault4"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/somersault5"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/somersault4"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/somersault3"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/somersault2"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/somersault1"))
        somersaultAnimation = Animation(.25f, animationImages, Animation.PlayMode.NORMAL)
        animationImages.clear()

        currentAnimation = idleAnimation
        setAnimation(idleAnimation)
    }

    private fun initializeClickBox() {
        clickBox = BaseActor(x, y, stage)
        clickBox.setSize(width * 2 * BaseGame.RATIO, height * 2)
        clickBox.centerAtActor(this)
        clickBox.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                revealHunter()
            }
        })
        /*clickBox.debug = true*/
    }

    fun revealHunter() {
        clickBox.touchable = Touchable.disabled
        inAction = true
        hidden = false
        rotation = 0f
        jump = true
        setSpeed(4f)
        setAnimation(somersaultAnimation)
        addAction(Actions.sequence(
            Actions.delay(.5f),
            Actions.rotateBy(360f, 1f))
        )

        val direction = if (MathUtils.randomBoolean()) 60f else 130f
        setMotionAngle(direction)

        Shot(x, y, stage, layerNumber)
        addAction(Actions.sequence(
            Actions.parallel(
                Actions.color(lightYellowBrown, .125f),
                Actions.scaleBy(revealScaleAmount, revealScaleAmount, 3f),
                Actions.sequence(
                    Actions.delay(.25f),
                    Actions.run { Shot(x, y, stage, layerNumber) },
                    Actions.delay(.5f),
                    Actions.run { Shot(x, y, stage, layerNumber) },
                    Actions.delay(.25f),
                    Actions.fadeOut(1.75f)
                )
            ),
            Actions.delay(2.5f),
            Actions.run { reset() }
        ))
    }

    fun reset() {
        inAction = false
        hidden = true
        jump = false
        rotation = 0f
        setAnimation(idleAnimation)
        setUpLayerAndPosition()
        setSpeed(0f)
        scaleBy(-revealScaleAmount, -revealScaleAmount)
        clickBox.touchable = Touchable.enabled
    }

    private fun setUpLayerAndPosition() {
        layerSetup()
        positionSetup()
    }

    private fun layerSetup() {
        for (layer in forestLayers)
            layer.touchable = Touchable.disabled
        layerNumber = MathUtils.random(1, 4)
        forestLayer = forestLayers[layerNumber]
        forestLayer.touchable = Touchable.enabled
        forestLayer.addActor(this)
        forestLayer.addActor(clickBox)
        color = forestLayer.color
    }

    private fun positionSetup() {
        val positions = getLayeredPositions()
        val randomPosition = MathUtils.random(0, positions.size - 1)
        setPosition(positions[randomPosition].position.x, positions[randomPosition].position.y)
        clickBox.centerAtActor(this)
        rotation = positions[randomPosition].rotation
    }

    private fun getLayeredPositions(): Array<Position> {
        val positions = Array<Position>()
        if (layerNumber == 4) {
            positions.add(Position(Vector2(30f, 66.5f), 0f))
            positions.add(Position(Vector2(56.2f, 52f), -15f))
            positions.add(Position(Vector2(75f, 74f), 0f))
        } else if (layerNumber == 3) {
            positions.add(Position(Vector2(47.5f, 71f), 0f))
            positions.add(Position(Vector2(73.4f, 50f), 10f))
            positions.add(Position(Vector2(30f, 83f), 0f))
            positions.add(Position(Vector2(84.4f, 82f), -170f))
        } else if (layerNumber == 2) {
            positions.add(Position(Vector2(5f, 65f), 0f))
            positions.add(Position(Vector2(92.1f, 30f), -12f))
        } else if (layerNumber == 1) {
            positions.add(Position(Vector2(66f, 50f), 10f))
            positions.add(Position(Vector2(27.4f, 39f), 10f))
            positions.add(Position(Vector2(17f, 73f), 0f))
        }
        return positions
    }

    class Position(position: Vector2, rotation: Float) {
        val position = position
        val rotation = rotation
    }
}
