package no.sandramoen.hunted.actors

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

class Hunter(s: Stage, forestLayers: Array<ForestLayer>) : BaseActor(0f, 0f, s) {
    private val forestLayers = forestLayers
    private var hunterIsOnLayer = MathUtils.random(1, 4)
    private var forestLayer = forestLayers[hunterIsOnLayer]

    private lateinit var clickBox: BaseActor
    private var jump = false
    private val revealScaleAmount = .5f

    init {
        loadImage("hunter")
        setSize(.5f, 1f * BaseGame.RATIO)
        setOrigin(Align.center)
        touchable = Touchable.disabled

        initClickBox(s)
        setUpLayerAndPosition()

        setAcceleration(10f)
        setMaxSpeed(5f)
        setDeceleration(10f)

        color = forestLayer.color
        /*color = Color.PINK*/
    }

    override fun act(dt: Float) {
        super.act(dt)
        if (jump)
            accelerateAtAngle(270f)
        applyPhysics(dt)
    }

    private fun initClickBox(s: Stage) {
        clickBox = BaseActor(x, y, s)
        clickBox.setSize(width * 2 * BaseGame.RATIO, height * 2)
        clickBox.centerAtActor(this)
        clickBox.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                revealHunter()
            }
        })
        forestLayer!!.addActor(clickBox)
        /*clickBox.debug = true*/
    }

    private fun revealHunter() {
        rotation = 0f
        jump = true
        setSpeed(4f)

        val direction = if (MathUtils.randomBoolean()) 60f else 130f
        setMotionAngle(direction)

        addAction(Actions.sequence(
            Actions.parallel(
                Actions.scaleBy(revealScaleAmount, revealScaleAmount, 3f),
                Actions.sequence(
                    Actions.delay(1f),
                    Actions.fadeOut(2f)
                )
            ),
            Actions.run { reset() }
        ))
    }

    private fun reset() {
        setUpLayerAndPosition()
        color.a = 1f
        jump = false
        color = forestLayer.color
        setSpeed(0f)
        scaleBy(-revealScaleAmount, -revealScaleAmount)
    }

    private fun setUpLayerAndPosition() {
        layerSetup()
        positionSetup()
    }

    private fun layerSetup() {
        for (layer in forestLayers)
            layer.touchable = Touchable.disabled
        hunterIsOnLayer = MathUtils.random(1, 4)
        forestLayer = forestLayers[hunterIsOnLayer]
        forestLayer.touchable = Touchable.enabled
        forestLayer.addActor(this)
        forestLayer.addActor(clickBox)
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
        if (hunterIsOnLayer == 4) {
            positions.add(Position(Vector2(30f, 66.5f), 0f))
            positions.add(Position(Vector2(56.2f, 52f), -15f))
            positions.add(Position(Vector2(75f, 74f), 0f))
        } else if (hunterIsOnLayer == 3) {
            positions.add(Position(Vector2(47.5f, 71f), 0f))
            positions.add(Position(Vector2(73.4f, 50f), 10f))
            positions.add(Position(Vector2(30f, 83f), 0f))
            positions.add(Position(Vector2(84.4f, 82f), -170f))
        } else if (hunterIsOnLayer == 2) {
            positions.add(Position(Vector2(5f, 65f), 0f))
            positions.add(Position(Vector2(92.1f, 30f), -12f))
        } else if (hunterIsOnLayer == 1) {
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
