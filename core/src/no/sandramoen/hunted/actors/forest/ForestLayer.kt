package no.sandramoen.hunted.actors.forest

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.actors.WaveActor
import no.sandramoen.hunted.actors.particles.SporesActor
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class ForestLayer(
    s: Stage,
    levelNumber: Int,
    layerNumber: Int,
    color: Color,
    var lightRayRotation: Float
) :
    BaseActor(0f, 0f, s) {
    lateinit var fog: FogLayer

    init {
        val path = "forest/level$levelNumber/Layer $layerNumber"
        this.color = color
        treeLayerInit(path)
        touchable = Touchable.disabled
        y = 105f

        if (layerNumber < 5) {
            treeLayerInit(path)
            addVines(path)
            addFog(path)
            addLeaves(layerNumber)
            addBirds(layerNumber)
            addLightRays(layerNumber)
            addMoose(layerNumber)
            addSpores(levelNumber, layerNumber)
        }

        if (layerNumber == 1) {
            initCoverUp(-19f, s)
            initCoverUp(98f, s)
        }
    }

    private fun addSpores(level: Int, layer: Int) {
        if (layer < 5) {
            val positions = Array<Vector2>()
            if (level == 1) {
                when (layer) {
                    4 -> {
                        positions.add(Vector2(27f, 32f))
                        positions.add(Vector2(66f, 55f))
                    }
                    3 -> {
                        positions.add(Vector2(6.5f, 27f))
                    }
                    2 -> {
                        positions.add(Vector2(37f, 41f))
                        positions.add(Vector2(73f, 47f))
                    }
                    1 -> {
                        positions.add(Vector2(51f, 50f))
                        positions.add(Vector2(17f, 18f))
                        positions.add(Vector2(81f, 30f))
                    }
                }
            } else if (level == 2) {
                when (layer) {
                    4 -> {
                        positions.add(Vector2(6.5f, 25f))
                        positions.add(Vector2(53f, 21f))
                    }
                    3 -> {
                        positions.add(Vector2(38.5f, 29.5f))
                        positions.add(Vector2(79.3f, 27.8f))
                    }
                    2 -> {
                        positions.add(Vector2(29f, 21f))
                        positions.add(Vector2(29.5f, 47f))
                        positions.add(Vector2(84f, 23f))
                    }
                    1 -> {
                        positions.add(Vector2(10.6f, 35f))
                        positions.add(Vector2(67f, 26f))
                    }
                }
            } else if (level == 3) {
                when (layer) {
                    4 -> {
                        positions.add(Vector2(92.5f, 46.5f))
                        positions.add(Vector2(47f, 26f))
                    }
                    3 -> {
                        positions.add(Vector2(70.3f, 44.8f))
                        positions.add(Vector2(71f, 18.5f))
                        positions.add(Vector2(31f, 49f))
                        positions.add(Vector2(2.5f, 22f))
                    }
                    2 -> {
                        positions.add(Vector2(80.8f, 22.5f))
                        positions.add(Vector2(32.4f, 17f))
                    }
                    1 -> {
                        positions.add(Vector2(58f, 28f))
                        positions.add(Vector2(14f, 30f))
                    }
                }
            } else if (level == 4) {
                when (layer) {
                    4 -> {
                        positions.add(Vector2(79f, 41f))
                        positions.add(Vector2(38f, 29f))
                    }
                    3 -> {
                        positions.add(Vector2(59.5f, 43.5f))
                        positions.add(Vector2(3f, 19f))
                    }
                    2 -> {
                        positions.add(Vector2(69.5f, 28f))
                        positions.add(Vector2(29f, 49f))
                    }
                    1 -> {
                        positions.add(Vector2(88f, 41f))
                        positions.add(Vector2(16f, 34f))
                        positions.add(Vector2(49f, 52f))
                    }
                }
            } else if (level == 5) {
                when (layer) {
                    4 -> {
                        positions.add(Vector2(41.5f, 27.5f))
                        positions.add(Vector2(72.5f, 30f))
                    }
                    3 -> {
                        positions.add(Vector2(83f, 28f))
                        positions.add(Vector2(46.5f, 41f))
                        positions.add(Vector2(22f, 18f))
                    }
                    2 -> {
                        positions.add(Vector2(3.5f, 45f))
                        positions.add(Vector2(92f, 18f))
                        positions.add(Vector2(32f, 37f))
                    }
                    1 -> {
                        positions.add(Vector2(11.5f, 33.5f))
                        positions.add(Vector2(53.5f, 25f))
                        positions.add(Vector2(54f, 41f))
                    }
                }
            } else if (level == 6) {
                when (layer) {
                    4 -> {
                        positions.add(Vector2(18f, 47f))
                        positions.add(Vector2(46.3f, 36.5f))
                        positions.add(Vector2(18.5f, 34f))
                        positions.add(Vector2(68f, 24f))
                    }
                    3 -> {
                        positions.add(Vector2(53.3f, 27f))
                        positions.add(Vector2(10f, 26f))
                    }
                    2 -> {
                        positions.add(Vector2(85.5f, 47f))
                        positions.add(Vector2(85.5f, 26f))
                        positions.add(Vector2(38.5f, 24f))
                        positions.add(Vector2(2f, 40.5f))
                    }
                    1 -> {
                        positions.add(Vector2(73.2f, 41.5f))
                        positions.add(Vector2(24.5f, 27f))
                    }
                }
            }

            for (i in 0 until positions.size)
                addActor(SporesActor(layer, positions[i].x, positions[i].y, stage, this))
        }
    }

    private fun treeLayerInit(path: String) {
        val treeLayer = BaseActor(0f, 0f, stage)
        treeLayer.loadImage(path)
        treeLayer.setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT)
        addActor(treeLayer)
    }

    private fun addVines(path: String) {
        addActor(WaveActor(0f, 0f, stage, "$path - vinesThin"))
        addActor(WaveActor(0f, 0f, stage, "$path - vinesMedium"))
        addActor(WaveActor(0f, 0f, stage, "$path - vinesThick"))
    }

    private fun addFog(path: String) {
        fog = FogLayer(0f, 0f, stage, "$path - fog")
        addActor(fog)
    }

    private fun addLeaves(layer: Int) {
        for (i in 0..MathUtils.random(25, 150))
            addActor(Leaf(stage, color, layer))
    }

    private fun addBirds(layer: Int) {
        for (i in 0 until MathUtils.random(0, 3))
            addActor(Bird(45f, 50f, stage, color, layer))
    }

    private fun addLightRays(layer: Int) {
        if (layer > 1)
            for (i in 0 until MathUtils.random(0, 2))
                addActor(LightRay(MathUtils.random(10f, 90f), 2f, stage, lightRayRotation))
    }

    private fun addMoose(layer: Int) {
        if (layer > 1 && MathUtils.random(0f, 1f) > .4)
            for (i in 0 until MathUtils.random(0, 1))
                addActor(Moose(stage, color, layer))
    }

    private fun initCoverUp(y: Float, s: Stage) {
        val actor = BaseActor(0f, y, s)
        actor.loadImage("whitePixel")
        actor.color = color
        actor.setSize(BaseGame.WORLD_WIDTH, 20f)
        addActor(actor)
    }
}
