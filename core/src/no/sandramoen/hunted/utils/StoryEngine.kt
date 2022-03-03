package no.sandramoen.hunted.utils

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Array

class StoryEngine(private val label: Label, private val storyLevel: Int, private val timerStartValue: Int, val stage: Stage) {
    private var typeWriterElapsedTime = 0f
    private var charactersPerSecond = 15f
    private var storyText = ""
    private var resetTypeWriter = { typeWriterElapsedTime = 0f }

    private var tutorial1Flag = true
    private var tutorial2Flag = true
    private var introFlag = true
    private var storyTriggerFlag = true

    private var interruptables = Array<Sound>()
    private val TUTORIAL = 1

    var pause = false

    init {
        label.addAction(Actions.fadeOut(0f))
        interruptables.add(BaseGame.level1_intro1VoiceSound)
        interruptables.add(BaseGame.level1_intro2VoiceSound)
    }

    fun update(dt: Float, timer: Int) {
        if (pause) return

        if (storyLevel == TUTORIAL) {
            if (timer == (timerStartValue * .95f).toInt() && tutorial1Flag) {
                timerTrigger(BaseGame.myBundle!!.get("level1_intro1"), BaseGame.level1_intro1VoiceSound)
                tutorial1Flag = false
            } else if (timer == (timerStartValue * .88f).toInt() && tutorial2Flag && storyLevel == 1) {
                timerTrigger(BaseGame.myBundle!!.get("level1_intro2"), BaseGame.level1_intro2VoiceSound)
                tutorial2Flag = false
            }
        } else {
            if (timer == (timerStartValue * .95f).toInt() && introFlag) {
                introTrigger()
                introFlag = false
            }
        }

        if (timer == (timerStartValue * .5f).toInt() && storyTriggerFlag) {
            storyTrigger()
            storyTriggerFlag = false
        }

        typeWriter(dt)
    }

    fun triggerHornSound() {
        trigger(BaseGame.myBundle!!.get("horn_blow"), BaseGame.horn_blowVoiceSound)
    }

    fun triggerFound(hunterSawPlayer: Boolean) {
        val randomNumber = MathUtils.random(0, 1)
        if (randomNumber == 0) {
            // sound!!.play(BaseGame.soundVolume) // TODO: play right sound for right story
            trigger(BaseGame.myBundle!!.get("outro1"), null)
        } else if (randomNumber == 1) {
            // sound!!.play(BaseGame.soundVolume) // TODO: play right sound for right story
            trigger(BaseGame.myBundle!!.get("outro2"), null)
        }

        val stopwatch = BaseActor(0f, 0f, stage)
        stopwatch.addAction(Actions.sequence(
            Actions.delay(1.25f),
            Actions.run {
                label.clearActions()
                // sound!!.play(BaseGame.soundVolume) // TODO: play right sound for right story
                trigger(BaseGame.myBundle!!.get("outro3"), null)
            }
        ))

        if (hunterSawPlayer) {
            stopwatch.addAction(Actions.sequence(
                Actions.delay(3.25f),
                Actions.run {
                    label.clearActions()
                    if (MathUtils.randomBoolean()) {
                        // sound!!.play(BaseGame.soundVolume) // TODO: play right sound for right story
                        trigger(BaseGame.myBundle!!.get("outroDanger0"), null)
                    } else {
                        // sound!!.play(BaseGame.soundVolume) // TODO: play right sound for right story
                        trigger(BaseGame.myBundle!!.get("outroDanger1"), null)
                    }
                }
            ))
        } else {
            stopwatch.addAction(Actions.sequence(
                Actions.delay(4f),
                Actions.run {
                    label.clearActions()
                    // sound!!.play(BaseGame.soundVolume) // TODO: play right sound for right story
                    trigger(BaseGame.myBundle!!.get("outroSafe"), null)
                }
            ))
        }
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
        sound?.play(BaseGame.soundVolume)
        for (interrupt in interruptables)
            interrupt.stop()
        fadeIn()
    }

    private fun introTrigger() {
        val randomNumber = MathUtils.random(0, 2)
        resetTypeWriter()
        this.storyText = BaseGame.myBundle!!.get("intro${randomNumber}")
        /*if (randomNumber == 0) // TODO: play right sound for right story
            sound!!.play(BaseGame.soundVolume)
        else if (randomNumber == 1)
            sound!!.play(BaseGame.soundVolume)
        else if (randomNumber == 2)
            sound!!.play(BaseGame.soundVolume)*/
        for (sound in interruptables)
            sound!!.stop()
        fadeIn()
    }

    private fun storyTrigger() {
        val randomNumber = MathUtils.random(0, 10)
        resetTypeWriter()
        this.storyText = BaseGame.myBundle!!.get("story${randomNumber}")
        /*if (randomNumber == 0) // TODO: play right sound for right story
            sound!!.play(BaseGame.soundVolume)
        else if (randomNumber == 1)
            sound!!.play(BaseGame.soundVolume)
        else if (randomNumber == 2)
            sound!!.play(BaseGame.soundVolume)*/
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
