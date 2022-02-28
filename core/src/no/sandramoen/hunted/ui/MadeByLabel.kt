package no.sandramoen.hunted.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.GameUtils

class MadeByLabel :
    Label("${BaseGame.myBundle!!.get("madeBy")} Sandra Moen 2022", BaseGame.labelStyle) {

    init {
        setFontScale(.75f)
        setAlignment(Align.center)
        color = Color.GRAY
        addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.clickSound!!.play(BaseGame.soundVolume)
                addAction(
                    Actions.sequence(
                        Actions.delay(.5f),
                        Actions.run { Gdx.net.openURI("https://sandramoen.no"); }
                    ))
            }
            false
        }
        GameUtils.addWidgetEnterExitEffect(this, BaseGame.lightPink, Color.GRAY)
    }
}
