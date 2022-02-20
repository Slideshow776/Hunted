package no.sandramoen.hunted.actors

import com.badlogic.gdx.scenes.scene2d.Stage
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame

class ForestLayer(stage: Stage, path: String) : BaseActor(0f, 0f, stage) {
    init {
        treeLayerInit(path)

        val layerNumber = path.substring(path.length-1).toInt()
        if (layerNumber < 5) {
            addActor(WaveActor(0f, 0f, stage, "$path - vinesThin"))
            addActor(WaveActor(0f, 0f, stage, "$path - vinesMedium"))
            addActor(WaveActor(0f, 0f, stage, "$path - vinesThick"))
            addActor(FogLayer(0f, 0f, stage, "$path - fog"))
        }
    }

    private fun treeLayerInit(path: String) {
        val treeLayer = BaseActor(0f, 0f, stage)
        treeLayer.loadImage(path)
        treeLayer.setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT)
        addActor(treeLayer)
    }
}
