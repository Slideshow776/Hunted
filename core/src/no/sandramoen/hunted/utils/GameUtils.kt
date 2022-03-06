package no.sandramoen.hunted.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class GameUtils {
    companion object {

        fun isTouchDownEvent(event: Event): Boolean { // Custom type checker
            return event is InputEvent && event.type == InputEvent.Type.touchDown
        }

        fun playAmbientMusicWithRandomStart() {
            BaseGame.ambient1Music!!.play()
            BaseGame.ambient1Music!!.volume = BaseGame.musicVolume
            BaseGame.ambient1Music!!.isLooping = true
            BaseGame.ambient1Music!!.position = MathUtils.random(0f, 97f)
        }

        fun shotTravelAmount(layer: Int): Float {
            return when (layer) {
                4 -> .75f
                3 -> 1f
                2 -> 2f
                else -> 2.75f
            }
        }

        fun playRandomLevelMusic() {
            if (MathUtils.randomBoolean())
                playAndLoopMusic(BaseGame.level1Music)
            else
                playAndLoopMusic(BaseGame.level2Music)
        }

        fun saveGameState() {
            BaseGame.prefs!!.putBoolean("loadPersonalParameters", true)
            BaseGame.prefs!!.putBoolean("googlePlayServices", BaseGame.isGPS)
            BaseGame.prefs!!.putFloat("musicVolume", BaseGame.musicVolume)
            BaseGame.prefs!!.putFloat("soundVolume", BaseGame.soundVolume)
            BaseGame.prefs!!.putString("locale", BaseGame.currentLocale)
            BaseGame.prefs!!.flush()
        }

        fun loadGameState() {
            BaseGame.prefs = Gdx.app.getPreferences("huntedGameState")
            BaseGame.loadPersonalParameters = BaseGame.prefs!!.getBoolean("loadPersonalParameters")
            BaseGame.isGPS = BaseGame.prefs!!.getBoolean("googlePlayServices")
            BaseGame.musicVolume = BaseGame.prefs!!.getFloat("musicVolume")
            BaseGame.soundVolume = BaseGame.prefs!!.getFloat("soundVolume")
            BaseGame.currentLocale = BaseGame.prefs!!.getString("locale")
        }

        fun stopAllMusic() {
            BaseGame.ambient1Music!!.stop()
            BaseGame.level1Music!!.stop()
            BaseGame.level2Music!!.stop()
            BaseGame.menuMusic!!.stop()
        }

        fun stopAmbientMusic() {
            BaseGame.ambient1Music!!.stop()
        }

        fun initShaderProgram(vertexShader: String?, fragmentShader: String?): ShaderProgram {
            ShaderProgram.pedantic = false
            val shaderProgram = ShaderProgram(vertexShader, fragmentShader)
            if (!shaderProgram.isCompiled)
                Gdx.app.error(javaClass.simpleName, "Couldn't compile shader: " + shaderProgram.log)
            return shaderProgram
        }

        fun setMusicVolume(volume: Float) {
            if (volume > 1f || volume < 0f)
                Gdx.app.error(javaClass.simpleName, "Volume needs to be within [0-1]. Volume is: $volume")
            BaseGame.musicVolume = volume
            BaseGame.level1Music!!.volume = BaseGame.musicVolume
            BaseGame.level2Music!!.volume = BaseGame.musicVolume
            BaseGame.ambient1Music!!.volume = BaseGame.musicVolume
            BaseGame.menuMusic!!.volume = BaseGame.musicVolume
        }

        fun playAndLoopMusic(music: Music?, volume: Float = BaseGame.musicVolume) {
            music!!.play()
            music!!.volume = volume
            music!!.isLooping = true
        }

        fun addTextButtonEnterExitEffect(textButton: TextButton, enterColor: Color = BaseGame.lightPink, exitColor: Color = Color.WHITE) {
            textButton.addListener(object : ClickListener() {
                override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                    textButton.label.addAction(Actions.color(enterColor, .125f))
                    BaseGame.hoverOverEnterSound!!.play(BaseGame.soundVolume)
                    super.enter(event, x, y, pointer, fromActor)
                }

                override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                    textButton.label.addAction(Actions.color(exitColor, .125f))
                    super.exit(event, x, y, pointer, toActor)
                }
            })
        }

        fun addWidgetEnterExitEffect(widget: Widget, enterColor: Color = BaseGame.lightPink, exitColor: Color = Color.WHITE) {
            widget.addListener(object : ClickListener() {
                override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                    widget.addAction(Actions.color(enterColor, .125f))
                    BaseGame.hoverOverEnterSound!!.play(BaseGame.soundVolume)
                    super.enter(event, x, y, pointer, fromActor)
                }

                override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                    widget.addAction(Actions.color(exitColor, .125f))
                    super.exit(event, x, y, pointer, toActor)
                }
            })
        }

        fun normalizeValue(value: Float, min: Float, max: Float): Float { return (value - min) / (max - min) }

        fun pulseWidget(actor: Actor, lowestAlpha: Float = .7f, duration: Float = 1f) {
            actor.addAction(Actions.forever(Actions.sequence(
                    Actions.alpha(lowestAlpha, duration / 2),
                    Actions.alpha(1f, duration / 2)
            )))
        }
    }
}
