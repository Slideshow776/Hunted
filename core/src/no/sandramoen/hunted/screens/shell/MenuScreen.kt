package no.sandramoen.hunted.screens.shell

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import no.sandramoen.hunted.actors.Hunter
import no.sandramoen.hunted.actors.MenuHunter
import no.sandramoen.hunted.actors.MenuNet
import no.sandramoen.hunted.actors.MenuShot
import no.sandramoen.hunted.actors.forest.ForestLayer
import no.sandramoen.hunted.screens.gameplay.LevelScreen
import no.sandramoen.hunted.ui.MadeByLabel
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.BaseScreen
import no.sandramoen.hunted.utils.GameUtils

class MenuScreen : BaseScreen() {
    private lateinit var startButton: TextButton
    private lateinit var optionsButton: TextButton
    private lateinit var titleLabel: Label

    private lateinit var hunter: MenuHunter

    override fun initialize() {
        titleLabel = Label("Hunted", BaseGame.labelStyle)
        titleLabel.setFontScale(3f)
        titleLabel.setAlignment(Align.center)

        hunter = MenuHunter(30f, 45f, mainStage)
        /*hunter.debug = true*/

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
        val buttonFontScale = 2f
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
        startButton.addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                hunter.addAction(Actions.moveTo(hunter.x, 45f, .1f))
                super.enter(event, x, y, pointer, fromActor)
            }
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                MenuShot(hunter.x, hunter.y + hunter.width / 2, mainStage)
                for (i in 0..MathUtils.random(0, 1))
                    hunter.addAction(Actions.sequence(
                        Actions.delay(MathUtils.random(.1f, .3f)),
                        Actions.run { MenuShot(hunter.x, hunter.y + hunter.width / 2, mainStage) }
                    ))
                super.clicked(event, x, y)
            }
        })

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
        optionsButton.addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                hunter.addAction(Actions.moveTo(hunter.x, 30f, .1f))
                super.enter(event, x, y, pointer, fromActor)
            }
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                MenuNet(hunter.x, hunter.y, mainStage)
                super.clicked(event, x, y)
            }
        })

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
        BaseGame.clickSound!!.play(BaseGame.soundVolume)
        titleLabel.addAction(Actions.sequence(
            Actions.delay(.5f),
                Actions.run {
                    super.dispose()
                    Gdx.app.exit()
                }
        ))
    }
}
