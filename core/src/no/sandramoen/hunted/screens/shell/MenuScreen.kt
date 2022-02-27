package no.sandramoen.hunted.screens.shell

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import no.sandramoen.hunted.screens.gameplay.LevelScreen
import no.sandramoen.hunted.ui.MadeByLabel
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.BaseScreen
import no.sandramoen.hunted.utils.GameUtils

class MenuScreen : BaseScreen() {
    private lateinit var startButton: TextButton
    private lateinit var optionsButton: TextButton
    private lateinit var titleLabel: Label

    override fun initialize() {
        // title
        titleLabel = Label("Hunted", BaseGame.labelStyle)
        titleLabel.setFontScale(1.25f)
        titleLabel.setAlignment(Align.center)

        // gui setup
        val table = Table()
        table.add(titleLabel).padTop(Gdx.graphics.height * .1f)
        table.row()
        table.add(menuButtonsTable()).fillY().expandY()
        table.row()
        table.add(MadeByLabel()).padBottom(Gdx.graphics.height * .02f)
        uiTable.add(table).fill().expand()
        /*table.debug = true*/
    }

    override fun update(dt: Float) {}

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE || keycode == Keys.BACKSPACE)
            exitGame()
        else if (keycode == Keys.ENTER)
            startGame()
        return false
    }

    private fun menuButtonsTable() : Table {
        val buttonFontScale = .8f
        startButton = TextButton("Start", BaseGame.textButtonStyle)
        startButton.label.setFontScale(buttonFontScale)
        startButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.clickSound!!.play(BaseGame.soundVolume)
                startGame()
            }
            false
        }
        GameUtils.addTextButtonEnterExitEffect(startButton)

        optionsButton = TextButton("Options", BaseGame.textButtonStyle)
        optionsButton.label.setFontScale(buttonFontScale)
        optionsButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.clickSound!!.play(BaseGame.soundVolume)
                changeToOptionsScreen()
            }
            false
        }
        GameUtils.addTextButtonEnterExitEffect(optionsButton)

        val table = Table()
        table.add(startButton).padBottom(Gdx.graphics.height * .03f).row()
        table.add(optionsButton).padBottom(Gdx.graphics.height * .03f)
        return table
    }

    private fun startGame() {
        startButton.addAction(Actions.sequence(
                Actions.delay(.5f),
                Actions.run { BaseGame.setActiveScreen(LevelScreen()) }
        ))
    }

    private fun changeToOptionsScreen() {
        optionsButton.addAction(Actions.sequence(
                Actions.delay(.5f),
                Actions.run { BaseGame.setActiveScreen(OptionsScreen()) }
        ))
    }

    private fun exitGame() {
        titleLabel.addAction(Actions.sequence(
                Actions.run {
                    super.dispose()
                    Gdx.app.exit()
                }
        ))
    }
}
