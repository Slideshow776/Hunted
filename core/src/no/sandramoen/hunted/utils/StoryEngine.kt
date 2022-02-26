package no.sandramoen.hunted.utils

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label

class StoryEngine(var label: Label, var timer: Float) {
    private var typeWriterElapsedTime = 0f
    private var charactersPerSecond = 15f
    private var storyText = ""
    private var resetTypeWriter = { typeWriterElapsedTime = 0f }

    var pause = false

    fun update(dt: Float, timer: Float) {
        if (pause) return

        this.timer = timer
        if (timer.toInt() == 58) {
            resetTypeWriter()
            storyText = BaseGame.myBundle!!.get("level1_intro1")
        } else if (timer.toInt() == 53) {
            resetTypeWriter()
            storyText = BaseGame.myBundle!!.get("level1_intro2")
        } else if (timer.toInt() == 30) {
            resetTypeWriter()
            storyText = BaseGame.myBundle!!.get("level_middle")
        }

        typeWriter(dt)
    }

    fun triggerHornSound() {
        resetTypeWriter()
        storyText = BaseGame.myBundle!!.get("horn_blow")
    }

    fun triggerFound() {
        resetTypeWriter
        storyText = BaseGame.myBundle!!.get("run")
    }

    fun triggerCaught() {
        resetTypeWriter()
        storyText = BaseGame.myBundle!!.get("caught")
    }

    private fun typeWriter(dt: Float): Boolean {
        typeWriterElapsedTime += dt
        var numberOfCharacters = (typeWriterElapsedTime * charactersPerSecond).toInt()
        if (numberOfCharacters > storyText.length)
            numberOfCharacters = storyText.length
        val partialText = storyText.substring(0, numberOfCharacters)
        if (partialText.isNotEmpty() && numberOfCharacters < storyText.length)
            label.addAction(Actions.fadeIn(1f))
        label.setText(partialText)

        if (numberOfCharacters >= storyText.length) {
            label.addAction(
                Actions.sequence(
                    Actions.delay(4f),
                    Actions.fadeOut(.25f)
                )
            )
        }

        return (numberOfCharacters >= storyText.length)
    }
}
