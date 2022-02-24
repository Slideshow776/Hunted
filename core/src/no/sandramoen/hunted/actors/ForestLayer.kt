package no.sandramoen.hunted.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class ForestLayer(stage: Stage, path: String, color: Color) : BaseActor(0f, 0f, stage) {
    init {
        this.color = color
        treeLayerInit(path)

        val layerNumber = path.substring(path.length - 1).toInt()
        if (layerNumber < 5) {
            addActor(WaveActor(0f, 0f, stage, "$path - vinesThin"))
            addActor(WaveActor(0f, 0f, stage, "$path - vinesMedium"))
            addActor(WaveActor(0f, 0f, stage, "$path - vinesThick"))
            addActor(FogLayer(0f, 0f, stage, "$path - fog"))

            for (i in 0..MathUtils.random(25, 150))
                addActor(Leaf(50f, 50f, stage, color, layerNumber))
        }
    }

    private fun treeLayerInit(path: String) {
        val treeLayer = BaseActor(0f, 0f, stage)
        treeLayer.loadImage(path)
        treeLayer.setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT)
        addActor(treeLayer)
    }
}
