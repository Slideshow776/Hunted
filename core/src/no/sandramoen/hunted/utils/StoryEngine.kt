package no.sandramoen.hunted.utils

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label

class StoryEngine(var label: Label, var timer: Float) {
    private var typeWriterElapsedTime = 0f
    private var charactersPerSecond = 15f
    private var storyText = ""
    private var resetTypeWriter = { typeWriterElapsedTime = 0f }

    fun update(dt: Float, timer: Float) {
        this.timer = timer
        if (timer.toInt() == 58) {
            resetTypeWriter()
            storyText = "You're being hunted!"
        } else if (timer.toInt() == 30) {
            resetTypeWriter()
            storyText = "It's still out there..."
        }

        typeWriter(dt)
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
                Actions.delay(2f),
                Actions.fadeOut(.25f)
            ))
        }

        return (numberOfCharacters >= storyText.length)
    }
}
