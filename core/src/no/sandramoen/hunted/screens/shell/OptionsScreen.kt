package no.sandramoen.hunted.screens.shell

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import com.badlogic.gdx.utils.Align
import no.sandramoen.hunted.actors.Vignette
import no.sandramoen.hunted.actors.forest.Leaf
import no.sandramoen.hunted.actors.hunter.OptionsHunter
import no.sandramoen.hunted.ui.*
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.BaseScreen
import no.sandramoen.hunted.utils.GameUtils

class OptionsScreen : BaseScreen() {
    private var gpsTable = GPSTable()

    override fun initialize() {
        addLeaves()
        OptionsHunter(mainStage)
        Vignette(mainStage)

        val table = Table()
        table.add(mainLabel())
        table.row()
        table.add(optionsTable()).fillY().expandY()
        table.row()
        table.add(backButton()).padBottom(Gdx.graphics.height * .02f)
        table.row()
        table.add(MadeByLabel()).padBottom(Gdx.graphics.height * .02f)
        /*table.debug = true*/

        uiTable.add(table).fill().expand()
    }

    override fun update(dt: Float) {
        gpsTable.update()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACKSPACE) {
            BaseGame.clickSound!!.play(BaseGame.soundVolume)
            BaseGame.setActiveScreen(MenuScreen())
        }
        return false
    }

    private fun optionsTable(): Table {
        val table = Table()
        if (BaseGame.skin != null) {
            table.add(BaseSlider("sound", "Sound")).padBottom(Gdx.graphics.height * .01f)
            table.row()
            table.add(BaseSlider("music", "Music")).padBottom(Gdx.graphics.height * .05f)
            table.row()
        }

        if (Gdx.app.type == Application.ApplicationType.Android) {
            table.add(gpsTable).padBottom(Gdx.graphics.height * .01f).row()
        }

        /*table.debug = true*/
        return table
    }

    private fun mainLabel(): Label {
        val label = Label("Options", BaseGame.bigLabelStyle)
        label.setFontScale(.6f)
        label.setAlignment(Align.center)
        return label
    }

    private fun backButton(): TextButton {
        val textButton = TextButton("Back", BaseGame.textButtonStyle)
        textButton.label.setFontScale(1f)
        textButton.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                textButton.touchable = Touchable.disabled
                BaseGame.clickSound!!.play(BaseGame.soundVolume)
                textButton.addAction(Actions.sequence(
                    Actions.delay(.5f),
                    Actions.run { BaseGame.setActiveScreen(MenuScreen(playMusic = false)) }
                ))
            }
        })
        GameUtils.addTextButtonEnterExitEffect(textButton)
        return textButton
    }

    private fun addLeaves() {
        for (i in 2 until 4) {
            var color = Color.PINK
            when (i) {
                5 -> color = Color(0.627f, 0.867f, 0.827f, 1f)
                4 -> color = Color(0.435f, 0.69f, 0.718f, 1f)
                3 -> color = Color(0.341f, 0.498f, 0.616f, 1f)
                2 -> color = Color(0.29f, 0.341f, 0.525f, 1f)
                1 -> color = Color(0.243f, 0.231f, 0.4f, 1f)
            }

            for (i in 0..10)//MathUtils.random(500, 800))
                Leaf(mainStage, color, i, 4f)
        }
    }
}
