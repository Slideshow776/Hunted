package no.sandramoen.hunted.screens.shell

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import no.sandramoen.hunted.ui.*
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.BaseScreen
import no.sandramoen.hunted.utils.GameUtils

class OptionsScreen : BaseScreen() {
    private lateinit var soundLabel: Label
    private var achievementButton = AchievementButton()
    private var leaderboardsButton = LeaderboardsButton()
    private var gpsTable = GPSTable(achievementButton, leaderboardsButton)

    override fun initialize() {
        val mainLabel = Label("Options", BaseGame.labelStyle)
        mainLabel.setFontScale(1.5f)

        val table = Table()
        table.add(mainLabel).padTop(Gdx.graphics.height * .1f)
        table.row()
        table.add(buttonsTable()).fillY().expandY()
        table.row()
        table.add(MadeByLabel()).padBottom(Gdx.graphics.height * .02f)
        table.setFillParent(true)
        // table.debug = true
        uiTable.add(table).fill().expand()
    }

    override fun update(dt: Float) {
        gpsTable.update()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACKSPACE)
            BaseGame.setActiveScreen(MenuScreen())
        return false
    }

    private fun buttonsTable(): Table {
        val buttonsTable = Table()
        if (BaseGame.skin != null) {
            buttonsTable.add(BaseSlider("sound", "Sound")).padBottom(Gdx.graphics.height * .01f)
            buttonsTable.row()
            buttonsTable.add(BaseSlider("music", "Music"))
            buttonsTable.row()
            // buttonsTable.add(Label("", BaseGame.labelStyle)).padBottom(Gdx.graphics.height * .0225f).row()
        }

        /*if (Gdx.app.type == Application.ApplicationType.Android) {*/
            buttonsTable.add(gpsTable).colspan(2).padTop(Gdx.graphics.height * .05f).row()
            buttonsTable.add(achievementButton).padTop(Gdx.graphics.height * .03f).colspan(2).row()
            buttonsTable.add(leaderboardsButton).padTop(Gdx.graphics.height * .03f).colspan(2).row()
        /*}*/

        buttonsTable.add(Label("", BaseGame.labelStyle)).row()
        buttonsTable.add(backButton()).colspan(2)
        // buttonsTable.debug = true
        return buttonsTable
    }

    private fun backButton(): TextButton {
        val textButton = TextButton("Back", BaseGame.textButtonStyle)
        textButton.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                BaseGame.clickSound!!.play(BaseGame.soundVolume)
                textButton.addAction(Actions.sequence(
                    Actions.delay(.5f),
                    Actions.run { BaseGame.setActiveScreen(MenuScreen()) }
                ))
            }
        })
        GameUtils.addTextButtonEnterExitEffect(textButton)
        return textButton
    }
}
