package no.sandramoen.hunted.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.GameUtils

class BaseSlider(var value: Float, val label: Label) : Container<Slider>() {
    var sliderContainer: Container<Slider>? = null

    init {
        val optionsWidgetWidth = Gdx.graphics.width * .6f // value must be pre-determined for scaling
        val optionsWidgetHeight = Gdx.graphics.height * .015f // value must be pre-determined for scaling
        val optionsSliderScale = Gdx.graphics.height * .002f // makes sure scale is device adjustable-ish

        val slider = Slider(0f, 1f, .1f, false, BaseGame.skin)
        slider.value = BaseGame.musicVolume
        slider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                value = slider.value
                BaseGame.clickSound!!.play(BaseGame.musicVolume)
                GameUtils.saveGameState()
            }
        })
        sliderContainer = Container(slider)
        sliderContainer!!.isTransform = true
        sliderContainer!!.setOrigin(
            (optionsWidgetWidth * 5 / 6) / 2,
            optionsWidgetHeight / 2
        )
        setScale(optionsSliderScale)
        addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                label.color = BaseGame.lightPink
                super.enter(event, x, y, pointer, fromActor)
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                label.color = Color.WHITE
                super.exit(event, x, y, pointer, toActor)
            }
        })
    }
}
