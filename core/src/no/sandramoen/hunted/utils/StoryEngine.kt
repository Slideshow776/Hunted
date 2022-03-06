package no.sandramoen.hunted.utils

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Array

class StoryEngine(
    private val label: Label,
    private val tutorial: Boolean,
    private val timerStartValue: Int,
    val stage: Stage
) {
    private var typeWriterElapsedTime = 0f
    private var charactersPerSecond = 20f
    private var storyText = ""
    private var resetTypeWriter = { typeWriterElapsedTime = 0f }

    private var tutorial1Flag = true
    private var tutorial2Flag = true
    private var introFlag = true
    private var storyTriggerFlag = true
    private var isOutro = false

    private var interruptables = Array<Sound>()

    var pause = false

    init {
        label.addAction(Actions.fadeOut(0f))

        interruptables.add(BaseGame.en_caughtVoiceSound)
        interruptables.add(BaseGame.en_hornBlowVoiceSound)
        interruptables.add(BaseGame.en_tutorial0VoiceSound)
        interruptables.add(BaseGame.en_tutorial1VoiceSound)
        interruptables.add(BaseGame.en_outro0VoiceSound)
        interruptables.add(BaseGame.en_outro1VoiceSound)
        interruptables.add(BaseGame.en_outro2VoiceSound)
        interruptables.add(BaseGame.en_outroSafeVoiceSound)
        interruptables.add(BaseGame.en_outroDanger0VoiceSound)
        interruptables.add(BaseGame.en_outroDanger1VoiceSound)
        interruptables.add(BaseGame.en_intro0VoiceSound)
        interruptables.add(BaseGame.en_intro1VoiceSound)
        interruptables.add(BaseGame.en_intro2VoiceSound)
        interruptables.add(BaseGame.en_story0VoiceSound)
        interruptables.add(BaseGame.en_story1VoiceSound)
        interruptables.add(BaseGame.en_story2VoiceSound)
        interruptables.add(BaseGame.en_story3VoiceSound)
        interruptables.add(BaseGame.en_story4VoiceSound)
        interruptables.add(BaseGame.en_story5VoiceSound)
        interruptables.add(BaseGame.en_story6VoiceSound)
        interruptables.add(BaseGame.en_story7VoiceSound)
        interruptables.add(BaseGame.en_story8VoiceSound)
        interruptables.add(BaseGame.en_story9VoiceSound)
        interruptables.add(BaseGame.en_story10VoiceSound)
        interruptables.add(BaseGame.no_caughtVoiceSound)
        interruptables.add(BaseGame.no_hornBlowVoiceSound)
        interruptables.add(BaseGame.no_tutorial0VoiceSound)
        interruptables.add(BaseGame.no_tutorial1VoiceSound)
        interruptables.add(BaseGame.no_outro0VoiceSound)
        interruptables.add(BaseGame.no_outro1VoiceSound)
        interruptables.add(BaseGame.no_outro2VoiceSound)
        interruptables.add(BaseGame.no_outroSafeVoiceSound)
        interruptables.add(BaseGame.no_outroDanger0VoiceSound)
        interruptables.add(BaseGame.no_outroDanger1VoiceSound)
        interruptables.add(BaseGame.no_intro0VoiceSound)
        interruptables.add(BaseGame.no_intro1VoiceSound)
        interruptables.add(BaseGame.no_intro2VoiceSound)
        interruptables.add(BaseGame.no_story0VoiceSound)
        interruptables.add(BaseGame.no_story1VoiceSound)
        interruptables.add(BaseGame.no_story2VoiceSound)
        interruptables.add(BaseGame.no_story3VoiceSound)
        interruptables.add(BaseGame.no_story4VoiceSound)
        interruptables.add(BaseGame.no_story5VoiceSound)
        interruptables.add(BaseGame.no_story6VoiceSound)
        interruptables.add(BaseGame.no_story7VoiceSound)
        interruptables.add(BaseGame.no_story8VoiceSound)
        interruptables.add(BaseGame.no_story9VoiceSound)
        interruptables.add(BaseGame.no_story10VoiceSound)
    }

    fun update(dt: Float, timer: Int) {
        if (pause) return

        if (tutorial) {
            if (timer == (timerStartValue * .95f).toInt() && tutorial1Flag) {
                if (BaseGame.currentLocale.equals("en"))
                    timerTrigger(
                        BaseGame.myBundle!!.get("tutorial0"),
                        BaseGame.en_tutorial0VoiceSound
                    )
                else if (BaseGame.currentLocale.equals("no"))
                    timerTrigger(
                        BaseGame.myBundle!!.get("tutorial0"),
                        BaseGame.no_tutorial0VoiceSound
                    )
                tutorial1Flag = false
            } else if (timer == (timerStartValue * .88f).toInt() && tutorial2Flag) {
                if (BaseGame.currentLocale.equals("en"))
                    timerTrigger(
                        BaseGame.myBundle!!.get("tutorial1"),
                        BaseGame.en_tutorial1VoiceSound
                    )
                else if (BaseGame.currentLocale.equals("no"))
                    timerTrigger(
                        BaseGame.myBundle!!.get("tutorial1"),
                        BaseGame.no_tutorial1VoiceSound
                    )
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

    fun triggerHornBlown() {
        if (!isOutro)
            if (BaseGame.currentLocale.equals("en"))
                trigger(BaseGame.myBundle!!.get("hornBlow"), BaseGame.en_hornBlowVoiceSound)
            else if (BaseGame.currentLocale.equals("no"))
                trigger(BaseGame.myBundle!!.get("hornBlow"), BaseGame.no_hornBlowVoiceSound)
    }

    fun triggerFound(hunterSawPlayer: Boolean) {
        isOutro = true
        stopAllSounds()
        val randomNumber = MathUtils.random(0, 1)
        if (randomNumber == 0) {
            if (BaseGame.currentLocale.equals("en"))
                trigger(BaseGame.myBundle!!.get("outro0"), BaseGame.en_outro0VoiceSound)
            else if (BaseGame.currentLocale.equals("no"))
                trigger(BaseGame.myBundle!!.get("outro0"), BaseGame.no_outro0VoiceSound)

        } else if (randomNumber == 1) {
            if (BaseGame.currentLocale.equals("en"))
                trigger(BaseGame.myBundle!!.get("outro1"), BaseGame.en_outro1VoiceSound)
            else if (BaseGame.currentLocale.equals("no"))
                trigger(BaseGame.myBundle!!.get("outro1"), BaseGame.no_outro1VoiceSound)
        }

        val stopwatch = BaseActor(0f, 0f, stage)
        stopwatch.addAction(Actions.sequence(
            Actions.delay(1.25f),
            Actions.run {
                label.clearActions()
                if (BaseGame.currentLocale.equals("en"))
                    trigger(BaseGame.myBundle!!.get("outro2"), BaseGame.en_outro2VoiceSound)
                else if (BaseGame.currentLocale.equals("no"))
                    trigger(BaseGame.myBundle!!.get("outro2"), BaseGame.no_outro2VoiceSound)
            }
        ))

        if (hunterSawPlayer) {
            stopwatch.addAction(Actions.sequence(
                Actions.delay(3.25f),
                Actions.run {
                    label.clearActions()
                    if (MathUtils.randomBoolean()) {
                        if (BaseGame.currentLocale.equals("en"))
                            trigger(
                                BaseGame.myBundle!!.get("outroDanger0"),
                                BaseGame.en_outroDanger0VoiceSound
                            )
                        else if (BaseGame.currentLocale.equals("no"))
                            trigger(
                                BaseGame.myBundle!!.get("outroDanger0"),
                                BaseGame.no_outroDanger0VoiceSound
                            )
                    } else {
                        if (BaseGame.currentLocale.equals("en"))
                            trigger(
                                BaseGame.myBundle!!.get("outroDanger1"),
                                BaseGame.en_outroDanger1VoiceSound
                            )
                        else if (BaseGame.currentLocale.equals("no"))
                            trigger(
                                BaseGame.myBundle!!.get("outroDanger1"),
                                BaseGame.no_outroDanger1VoiceSound
                            )
                    }
                }
            ))
        } else {
            stopwatch.addAction(Actions.sequence(
                Actions.delay(4f),
                Actions.run {
                    label.clearActions()
                    if (BaseGame.currentLocale.equals("en"))
                        trigger(BaseGame.myBundle!!.get("outroSafe"), BaseGame.en_outroSafeVoiceSound)
                    else if (BaseGame.currentLocale.equals("no"))
                        trigger(BaseGame.myBundle!!.get("outroSafe"), BaseGame.no_outroSafeVoiceSound)
                }
            ))
        }
    }

    fun triggerCaught() {
        if (BaseGame.currentLocale.equals("en"))
            trigger(BaseGame.myBundle!!.get("caught"), BaseGame.en_caughtVoiceSound)
        else if (BaseGame.currentLocale.equals("no"))
            trigger(BaseGame.myBundle!!.get("caught"), BaseGame.no_caughtVoiceSound)
    }

    fun stopAllSounds() {
        for (sound in interruptables)
            sound!!.stop()
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
        fadeIn()
    }

    private fun introTrigger() {
        stopAllSounds()
        val randomNumber = MathUtils.random(0, 2)
        resetTypeWriter()
        this.storyText = BaseGame.myBundle!!.get("intro${randomNumber}")
        if (BaseGame.currentLocale.equals("en"))
            when (randomNumber) {
                0 -> BaseGame.en_intro0VoiceSound!!.play(BaseGame.soundVolume)
                1 -> BaseGame.en_intro1VoiceSound!!.play(BaseGame.soundVolume)
                2 -> BaseGame.en_intro2VoiceSound!!.play(BaseGame.soundVolume)
            }
        else if (BaseGame.currentLocale.equals("no"))
            when (randomNumber) {
                0 -> BaseGame.no_intro0VoiceSound!!.play(BaseGame.soundVolume)
                1 -> BaseGame.no_intro1VoiceSound!!.play(BaseGame.soundVolume)
                2 -> BaseGame.no_intro2VoiceSound!!.play(BaseGame.soundVolume)
            }
        fadeIn()
    }

    fun storyTrigger() {
        stopAllSounds()
        val randomNumber = MathUtils.random(0, 10)
        resetTypeWriter()
        this.storyText = BaseGame.myBundle!!.get("story${randomNumber}")
        if (BaseGame.currentLocale.equals("en"))
            when (randomNumber) {
                0 -> BaseGame.en_story0VoiceSound!!.play(BaseGame.soundVolume)
                1 -> BaseGame.en_story1VoiceSound!!.play(BaseGame.soundVolume)
                2 -> BaseGame.en_story2VoiceSound!!.play(BaseGame.soundVolume)
                3 -> BaseGame.en_story3VoiceSound!!.play(BaseGame.soundVolume)
                4 -> BaseGame.en_story4VoiceSound!!.play(BaseGame.soundVolume)
                5 -> BaseGame.en_story5VoiceSound!!.play(BaseGame.soundVolume)
                6 -> BaseGame.en_story6VoiceSound!!.play(BaseGame.soundVolume)
                7 -> BaseGame.en_story7VoiceSound!!.play(BaseGame.soundVolume)
                8 -> BaseGame.en_story8VoiceSound!!.play(BaseGame.soundVolume)
                9 -> BaseGame.en_story9VoiceSound!!.play(BaseGame.soundVolume)
                10 -> BaseGame.en_story10VoiceSound!!.play(BaseGame.soundVolume)
            }
        else if (BaseGame.currentLocale.equals("no"))
            when (randomNumber) {
                0 -> BaseGame.no_story0VoiceSound!!.play(BaseGame.soundVolume)
                1 -> BaseGame.no_story1VoiceSound!!.play(BaseGame.soundVolume)
                2 -> BaseGame.no_story2VoiceSound!!.play(BaseGame.soundVolume)
                3 -> BaseGame.no_story3VoiceSound!!.play(BaseGame.soundVolume)
                4 -> BaseGame.no_story4VoiceSound!!.play(BaseGame.soundVolume)
                5 -> BaseGame.no_story5VoiceSound!!.play(BaseGame.soundVolume)
                6 -> BaseGame.no_story6VoiceSound!!.play(BaseGame.soundVolume)
                7 -> BaseGame.no_story7VoiceSound!!.play(BaseGame.soundVolume)
                8 -> BaseGame.no_story8VoiceSound!!.play(BaseGame.soundVolume)
                9 -> BaseGame.no_story9VoiceSound!!.play(BaseGame.soundVolume)
                10 -> BaseGame.no_story10VoiceSound!!.play(BaseGame.soundVolume)
            }
        fadeIn()
    }

    private fun timerTrigger(storyText: String, sound: Sound?) {
        stopAllSounds()
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
