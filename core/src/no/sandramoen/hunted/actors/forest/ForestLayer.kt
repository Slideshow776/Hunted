package no.sandramoen.hunted.actors.forest

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import no.sandramoen.hunted.actors.WaveActor
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class ForestLayer(s: Stage, path: String, color: Color, var lightRayRotation: Float) :
    BaseActor(0f, 0f, s) {
    lateinit var fog: FogLayer

    init {
        this.color = color
        treeLayerInit(path)
        touchable = Touchable.disabled
        y = 105f

        val layer = path.substring(path.length - 1).toInt()
        if (layer < 5) {
            treeLayerInit(path)
            addVines(path)
            addFog(path)
            addLeaves(layer)
            addBirds(layer)
            addLightRays(layer)
        }

        if (layer == 1) {
            initCoverUp(-19f, s)
            initCoverUp(98f, s)
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

    private fun initCoverUp(y: Float, s: Stage) {
        val actor = BaseActor(0f, y, s)
        actor.loadImage("whitePixel")
        actor.color = color
        actor.setSize(BaseGame.WORLD_WIDTH, 20f)
        addActor(actor)
    }
}
