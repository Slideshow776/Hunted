package no.sandramoen.hunted.actors.forest

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import no.sandramoen.hunted.actors.WaveActor
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class ForestLayer(stage: Stage, path: String, color: Color, var lightRayRotation: Float) :
    BaseActor(0f, 0f, stage) {
    lateinit var fog: FogLayer

    init {
        this.color = color
        treeLayerInit(path)

        val layer = path.substring(path.length - 1).toInt()
        if (layer < 5) {
            treeLayerInit(path)
            addVines(path)
            addFog(path)
            addLeaves(layer)
            addBirds(layer)
            addLightRays(layer)
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
            addActor(Leaf(50f, 50f, stage, color, layer))
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
}