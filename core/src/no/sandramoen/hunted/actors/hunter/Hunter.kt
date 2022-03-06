package no.sandramoen.hunted.actors.hunter

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.actors.Shot
import no.sandramoen.hunted.actors.forest.ForestLayer
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.GameUtils
import no.sandramoen.hunted.utils.GameUtils.Companion.isTouchDownEvent

class Hunter(stage: Stage, forestLayers: Array<ForestLayer>, val levelNumber: Int) :
    BaseActor(0f, 0f, stage) {
    private val forestLayers = forestLayers
    private val revealScaleAmount = .5f

    private lateinit var forestLayer: ForestLayer
    private lateinit var idleAnimation: Animation<TextureAtlas.AtlasRegion>
    private lateinit var somersaultAnimation: Animation<TextureAtlas.AtlasRegion>
    private lateinit var playHornAnimation: Animation<TextureAtlas.AtlasRegion>
    private lateinit var putHornAwayAnimation: Animation<TextureAtlas.AtlasRegion>

    private var jump = false

    lateinit var clickBox: BaseActor

    var isHidden = true
    var detectedPlayer = false
    var layerNumber: Int = -1
    var isNotBlowingHorn = true

    init {
        animationSetUp()

        initializeClickBox()
        setUpLayerPositionAndSize()

        setAcceleration(10f)
        setMaxSpeed(5f)
        setDeceleration(10f)

        slowBreathing()
        touchable = Touchable.disabled

        // color = Color.PINK // debug
        /*debug = true*/
    }

    override fun act(dt: Float) {
        super.act(dt)
        if (jump)
            accelerateAtAngle(270f)
        applyPhysics(dt)
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

    fun reset() {
        detectedPlayer = false
        isHidden = true
        jump = false
        rotation = 0f
        setAnimation(idleAnimation)
        setUpLayerPositionAndSize()
        setSpeed(0f)
        scaleX = 1f
        scaleY = 1f
        clickBox.touchable = Touchable.enabled
    }

    private fun initializeClickBox() {
        clickBox = BaseActor(x, y, stage)
        clickBox.centerAtActor(this)
        clickBox.addListener { e: Event ->
            if (isTouchDownEvent(e)) {
                if (MathUtils.random(1, 4) > 1) {
                    isHidden = false
                } else {
                    isHidden = false
                    detectedPlayer = true
                    addAction(Actions.sequence(
                        Actions.delay(6.5f),
                        Actions.run { revealHunter() }
                    ))
                }
            }
            false
        }
    }

    private fun setSize() {
        val hunterScale = 1.05f
        when (layerNumber) {
            1 -> setSize(1.25f * hunterScale, 1f * BaseGame.RATIO * hunterScale)
            2 -> setSize(1.4f * hunterScale, 1.12f * BaseGame.RATIO * hunterScale)
            3 -> setSize(1.6f * hunterScale, 1.263f * BaseGame.RATIO * hunterScale)
            4 -> setSize(1.8f * hunterScale, 1.388f * BaseGame.RATIO * hunterScale)
        }
        setOrigin(Align.center)
        val clickScale = 2f
        clickBox.setSize(width * .75f * BaseGame.RATIO * clickScale, height * 2 * clickScale)
        clickBox.centerAtActor(this)
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

    private fun stopBlowingHorn() {
        addAction(Actions.sequence(
            Actions.delay(GameUtils.shotTravelAmount(layerNumber)),
            Actions.run { BaseGame.hornSound!!.stop() }
        ))
    }

    private fun revealHunter() {
        stopBlowingHorn()
        clickBox.touchable = Touchable.disabled
        addAction(Actions.rotateTo(0f, .125f))
        jump = true
        setSpeed(4f)

        val direction = if (MathUtils.randomBoolean()) 60f else 130f
        setMotionAngle(direction)

        setAnimation(somersaultAnimation)
        somersaultAction()
        Shot(x, y, stage, layerNumber)
        addAction(
            Actions.sequence(
                Actions.parallel(
                    Actions.color(BaseGame.lightYellowBrown, .125f),
                    Actions.scaleBy(revealScaleAmount, revealScaleAmount, 3f),
                    Actions.sequence(
                        Actions.delay(.25f),
                        Actions.run { Shot(x, y, stage, layerNumber) },
                        Actions.delay(.5f),
                        Actions.run { Shot(x, y, stage, layerNumber) },
                        Actions.delay(.75f),
                        Actions.fadeOut(1.75f)
                    )
                )
            )
        )
    }

    private fun setUpLayerPositionAndSize() {
        layerSetup()
        positionSetup()
        setSize()
    }

    private fun layerSetup() {
        for (layer in forestLayers)
            layer.touchable = Touchable.disabled
        layerNumber = MathUtils.random(1, 4)
        forestLayer = forestLayers[layerNumber]
        forestLayer.touchable = Touchable.enabled
        forestLayer.addActor(clickBox)
        forestLayer.addActor(this)
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
        if (levelNumber == 1) {
            when (layerNumber) {
                4 -> {
                    positions.add(Position(Vector2(30f, 67f), 0f))
                    positions.add(Position(Vector2(55.8f, 52f), -15f))
                    positions.add(Position(Vector2(75f, 73f), 0f))
                }
                3 -> {
                    positions.add(Position(Vector2(47.5f, 71.5f), 0f))
                    positions.add(Position(Vector2(73f, 50f), 10f))
                    positions.add(Position(Vector2(30f, 82.7f), 0f))
                    positions.add(Position(Vector2(84.4f, 84f), -170f))
                }
                2 -> {
                    positions.add(Position(Vector2(5f, 65f), 0f))
                    positions.add(Position(Vector2(91.5f, 30f), -12f))
                }
                1 -> {
                    positions.add(Position(Vector2(65.7f, 48.3f), 10f))
                    positions.add(Position(Vector2(17f, 74f), 0f))
                }
            }
        } else if (levelNumber == 2) {
            when (layerNumber) {
                4 -> {
                    positions.add(Position(Vector2(35f, 80f), 0f))
                    positions.add(Position(Vector2(10f, 85f), 0f))
                    positions.add(Position(Vector2(76.1f, 18f), -10f))
                    positions.add(Position(Vector2(40f, 90f), 180f))
                }
                3 -> {
                    positions.add(Position(Vector2(28f, 15f), 10f))
                    positions.add(Position(Vector2(22f, 82.7f), 3f))
                    positions.add(Position(Vector2(43.2f, 84.6f), 0f))
                    positions.add(Position(Vector2(43.2f, 84.6f), 0f))
                    positions.add(Position(Vector2(79.3f, 59.7f), 180f))
                    positions.add(Position(Vector2(96f, 64.7f), 0f))
                }
                2 -> {
                    positions.add(Position(Vector2(51.3f, 78f), 10f))
                    positions.add(Position(Vector2(29f, 83f), -5f))
                    positions.add(Position(Vector2(2f, 74f), 5f))
                    positions.add(Position(Vector2(80.5f, 81f), -15f))
                }
                1 -> {
                    positions.add(Position(Vector2(55f, 57.5f), 0f))
                    positions.add(Position(Vector2(6f, 63f), 0f))
                    positions.add(Position(Vector2(94f, 80.8f), 0f))
                }
            }
        } else if (levelNumber == 3) {
            when (layerNumber) {
                4 -> {
                    positions.add(Position(Vector2(53.8f, 73.7f), -10f))
                    positions.add(Position(Vector2(13.8f, 33.4f), 10f))
                    positions.add(Position(Vector2(96f, 92.5f), 170f))
                }
                3 -> {
                    positions.add(Position(Vector2(32.7f, 72.5f), 0f))
                    positions.add(Position(Vector2(89f, 54.7f), 195f))
                    positions.add(Position(Vector2(66.5f, 88f), 0f))
                    positions.add(Position(Vector2(37f, 76f), 0f))
                }
                2 -> {
                    positions.add(Position(Vector2(2.7f, 70.6f), -15f))
                    positions.add(Position(Vector2(29.8f, 81.7f), -15f))
                    positions.add(Position(Vector2(77.2f, 17.5f), -15f))
                    positions.add(Position(Vector2(86.2f, 70f), 0f))
                }
                1 -> {
                    positions.add(Position(Vector2(25f, 29f), -15f))
                    positions.add(Position(Vector2(28f, 66.5f), 0f))
                    positions.add(Position(Vector2(55f, 57.5f), 0f))
                    positions.add(Position(Vector2(92.5f, 51f), 10f))
                }
            }
        } else if (levelNumber == 4) {
            when (layerNumber) {
                4 -> {
                    positions.add(Position(Vector2(95f, 77.3f), 0f))
                    positions.add(Position(Vector2(58.5f, 81.5f), 190f))
                    positions.add(Position(Vector2(47f, 12f), 10f))
                    positions.add(Position(Vector2(15.8f, 78f), 10f))
                }
                3 -> {
                    positions.add(Position(Vector2(6f, 76f), 0f))
                    positions.add(Position(Vector2(36.5f, 42f), -10f))
                    positions.add(Position(Vector2(76.4f, 74.8f), 0f))
                }
                2 -> {
                    positions.add(Position(Vector2(15.5f, 76.5f), 0f))
                    positions.add(Position(Vector2(59.5f, 74f), 0f))
                    positions.add(Position(Vector2(78f, 85f), 195f))
                }
                1 -> {
                    positions.add(Position(Vector2(44f, 26f), -15f))
                    positions.add(Position(Vector2(1.5f, 57.5f), 0f))
                    positions.add(Position(Vector2(94f, 67f), 0f))
                    positions.add(Position(Vector2(42f, 82f), 0f))
                }
            }
        } else if (levelNumber == 5) {
            when (layerNumber) {
                4 -> {
                    positions.add(Position(Vector2(71f, 80f), -20f))
                    positions.add(Position(Vector2(53.5f, 61f), 10f))
                    positions.add(Position(Vector2(19f, 57f), 0f))
                }
                3 -> {
                    positions.add(Position(Vector2(2.8f, 54f), 10f))
                    positions.add(Position(Vector2(37f, 69f), 0f))
                    positions.add(Position(Vector2(67f, 83.5f), 180f))
                    positions.add(Position(Vector2(91.8f, 77f), -5f))
                }
                2 -> {
                    positions.add(Position(Vector2(80f, 64f), 0f))
                    positions.add(Position(Vector2(94.3f, 71.6f), 0f))
                    positions.add(Position(Vector2(30f, 13.3f), 0f))
                    positions.add(Position(Vector2(19f, 75.5f), -10f))
                }
                1 -> {
                    positions.add(Position(Vector2(44.6f, 63f), 0f))
                    positions.add(Position(Vector2(68.4f, 62.4f), 5f))
                    positions.add(Position(Vector2(84f, 72.4f), 5f))
                }
            }
        } else if (levelNumber == 6) {
            when (layerNumber) {
                4 -> {
                    positions.add(Position(Vector2(31.5f, 6.7f), -10f))
                    positions.add(Position(Vector2(23.5f, 55.5f), 170f))
                    positions.add(Position(Vector2(78.3f, 54f), -15f))
                }
                3 -> {
                    positions.add(Position(Vector2(43f, 53f), 0f))
                    positions.add(Position(Vector2(1.5f, 33f), 10f))
                    positions.add(Position(Vector2(91f, 35f), -10f))
                }
                2 -> {
                    positions.add(Position(Vector2(16.5f, 33f), -15f))
                    positions.add(Position(Vector2(53f, 18f), 10f))
                    positions.add(Position(Vector2(64f, 56f), 0f))
                    positions.add(Position(Vector2(39.2f, 79f), 0f))
                }
                1 -> {
                    positions.add(Position(Vector2(4.7f, 80.4f), 190f))
                    positions.add(Position(Vector2(21.1f, 37f), -10f))
                    positions.add(Position(Vector2(44f, 75f), 0f))
                    positions.add(Position(Vector2(65f, 70f), -5f))
                }
            }
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
