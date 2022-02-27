package no.sandramoen.hunted.utils

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Array

class StoryEngine(var label: Label) {
    private var typeWriterElapsedTime = 0f
    private var charactersPerSecond = 15f
    private var storyText = ""
    private var resetTypeWriter = { typeWriterElapsedTime = 0f }

    private var level1_intro1Flag = true
    private var level1_intro2 = true
    private var level_middle = true

    private var interruptables = Array<Sound>()

    var pause = false

    init {
        label.addAction(Actions.fadeOut(0f))
        interruptables.add(BaseGame.level1_intro1VoiceSound)
        interruptables.add(BaseGame.level1_intro2VoiceSound)
        interruptables.add(BaseGame.level_middleVoiceSound)
    }

    fun update(dt: Float, timer: Float) {
        if (pause) return

        if (timer.toInt() == 58 && level1_intro1Flag) {
            timerTrigger(BaseGame.myBundle!!.get("level1_intro1"), BaseGame.level1_intro1VoiceSound)
            level1_intro1Flag = false
        } else if (timer.toInt() == 53 && level1_intro2) {
            timerTrigger(BaseGame.myBundle!!.get("level1_intro2"), BaseGame.level1_intro2VoiceSound)
            level1_intro2 = false
        } else if (timer.toInt() == 30 && level_middle) {
            timerTrigger(BaseGame.myBundle!!.get("level_middle"), BaseGame.level_middleVoiceSound)
            level_middle = false
        }

        typeWriter(dt)
    }

    fun triggerHornSound() {
        trigger(BaseGame.myBundle!!.get("horn_blow"), BaseGame.horn_blowVoiceSound)
    }

    fun triggerFound() {
        trigger(BaseGame.myBundle!!.get("run"), BaseGame.runVoiceSound)
    }

    fun triggerCaught() {
        trigger(BaseGame.myBundle!!.get("caught"), BaseGame.caughtVoiceSound)
    }

    private fun typeWriter(dt: Float) {
        typeWriterElapsedTime += dt
        var numberOfCharacters = (typeWriterElapsedTime * charactersPerSecond).toInt()
        if (numberOfCharacters > storyText.length)
            numberOfCharacters = storyText.length
        val partialText = storyText.substring(0, numberOfCharacters)
        label.setText(partialText)

        if (numberOfCharacters >= storyText.length && storyText.isNotEmpty())
            fadeOut()
    }

    private fun trigger(storyText: String, sound: Sound?) {
        resetTypeWriter()
        this.storyText = storyText
        sound!!.play(BaseGame.soundVolume)
        for (sound in interruptables)
            sound!!.stop()
        fadeIn()
    }

    private fun timerTrigger(storyText: String, sound: Sound?) {
        resetTypeWriter()
        this.storyText = storyText
        sound!!.play(BaseGame.soundVolume)
        fadeIn()
    }

    private fun fadeIn() {
        label.addAction(Actions.fadeIn(1f))
    }

    private fun fadeOut() {
        if (label.actions.isEmpty && label.color.a > 0) {
            label.addAction(
                Actions.sequence(
                    Actions.delay(1f),
                    Actions.fadeOut(1f)
                )
            )
        }
    }
}
