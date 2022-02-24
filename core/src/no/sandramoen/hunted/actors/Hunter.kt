package no.sandramoen.hunted.actors

import com.badlogic.gdx.Gdx
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
import no.sandramoen.hunted.utils.GameUtils

class Hunter(stage: Stage, forestLayers: Array<ForestLayer>) : BaseActor(0f, 0f, stage) {
    private val forestLayers = forestLayers
    private val revealScaleAmount = .5f

    private lateinit var forestLayer: ForestLayer
    lateinit var clickBox: BaseActor
    private lateinit var idleAnimation: Animation<TextureAtlas.AtlasRegion>
    private lateinit var somersaultAnimation: Animation<TextureAtlas.AtlasRegion>
    private lateinit var playHornAnimation: Animation<TextureAtlas.AtlasRegion>
    private lateinit var putHornAwayAnimation: Animation<TextureAtlas.AtlasRegion>

    private var jump = false
    private var lightYellowBrown = Color(0.969f, 0.812f, 0.569f, 1f)

    var isHidden = true
    var inAction = false
    var layerNumber: Int = -1
    var isNotBlowingHorn = true

    init {
        animationSetUp()
        val scale = 1f
        setSize(1.25f * scale, 1f * BaseGame.RATIO * scale)
        setOrigin(Align.center)
        touchable = Touchable.disabled

        initializeClickBox()
        setUpLayerAndPosition()

        setAcceleration(10f)
        setMaxSpeed(5f)
        setDeceleration(10f)

        slowBreathing()

        // color = Color.PINK // debug
    }

    override fun act(dt: Float) {
        super.act(dt)
        if (jump)
            accelerateAtAngle(270f)
        applyPhysics(dt)
    }

    private fun slowBreathing() {
        addAction(Actions.forever(Actions.sequence(
            Actions.scaleBy(0f, .125f, 4f),
            Actions.scaleBy(0f, -.125f, 4f)
        )))
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

        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/horn1"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/horn2"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/horn3"))
        playHornAnimation = Animation(.5f, animationImages, Animation.PlayMode.NORMAL)
        animationImages.clear()

        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/horn3"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/horn2"))
        animationImages.add(BaseGame.textureAtlas!!.findRegion("hunter/horn1"))
        putHornAwayAnimation = Animation(.5f, animationImages, Animation.PlayMode.NORMAL)
        animationImages.clear()

        setAnimation(idleAnimation)
    }

    private fun initializeClickBox() {
        clickBox = BaseActor(x, y, stage)
        clickBox.setSize(width * .75f * BaseGame.RATIO, height * 2)
        clickBox.centerAtActor(this)
        clickBox.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                revealHunter()
            }
        })
        /*clickBox.debug = true*/
    }

    fun blowHorn() {
        if (isHidden) {
            isNotBlowingHorn = false
            setAnimation(playHornAnimation)
            addAction(Actions.sequence(
                Actions.delay(.6f),
                Actions.run { BaseGame.hornSound!!.play(BaseGame.soundVolume) },
                Actions.delay(4f),
                Actions.run { setAnimation(putHornAwayAnimation) },
                Actions.delay(1.8f),
                Actions.run {
                    setAnimation(idleAnimation)
                    isNotBlowingHorn = true
                }
            ))
        } else Gdx.app.error(javaClass.simpleName, "Horn was blown when not hidden...")
    }

    fun revealHunter() {
        addAction(Actions.sequence(
            Actions.delay(GameUtils.shotTravelAmount(layerNumber)),
            Actions.run { BaseGame.hornSound!!.stop() }
        ))
        clickBox.touchable = Touchable.disabled
        inAction = true
        isHidden = false
        addAction(Actions.rotateTo(0f, .125f))
        jump = true
        setSpeed(4f)

        val direction = if (MathUtils.randomBoolean()) 60f else 130f
        setMotionAngle(direction)

        setAnimation(somersaultAnimation)
        somersaultAction()
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
                    Actions.delay(.75f),
                    Actions.fadeOut(1.75f)
                )
            ),
            Actions.delay(2.5f),
            Actions.run { reset() }
        ))
    }

    fun reset() {
        inAction = false
        isHidden = true
        jump = false
        rotation = 0f
        setAnimation(idleAnimation)
        setUpLayerAndPosition()
        setSpeed(0f)
        scaleX = 1f
        scaleY = 1f
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
            positions.add(Position(Vector2(30f, 67f), 0f))
            positions.add(Position(Vector2(55.8f, 52f), -15f))
            positions.add(Position(Vector2(75f, 74f), 0f))
        } else if (layerNumber == 3) {
            positions.add(Position(Vector2(47.5f, 71.5f), 0f))
            positions.add(Position(Vector2(73f, 50f), 10f))
            positions.add(Position(Vector2(30f, 83f), 0f))
            positions.add(Position(Vector2(84.4f, 84f), -170f))
        } else if (layerNumber == 2) {
            positions.add(Position(Vector2(5f, 65f), 0f))
            positions.add(Position(Vector2(91.5f, 30f), -12f))
        } else if (layerNumber == 1) {
            positions.add(Position(Vector2(65.7f, 48.3f), 10f))
            positions.add(Position(Vector2(17f, 74f), 0f))
        }
        return positions
    }

    private fun somersaultAction() {
        addAction(
            Actions.sequence(
                Actions.delay(.5f),
                Actions.rotateBy(360f, 1f)
            )
        )
    }

    class Position(position: Vector2, rotation: Float) {
        val position = position
        val rotation = rotation
    }
}
