package no.sandramoen.hunted.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import no.sandramoen.hunted.utils.BaseActor
import no.sandramoen.hunted.utils.BaseGame
import no.sandramoen.hunted.utils.GameUtils

class WaveActor(
    x: Float, y: Float, s: Stage, path: String,
    amplitude: Float = .15f,
    waveLength: Float = 10f,
    velocity: Float = .5f
) : BaseActor(x, y, s) {

    private var shaderProgram: ShaderProgram
    private var time = 0f
    var amplitudeX = amplitude
    var waveLengthX = waveLength
    var velocityX = velocity
    var amplitudeY = amplitude
    var waveLengthY = waveLength
    var velocityY = velocity

    init {
        loadImage("$path")
        setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT)
        shaderProgram = GameUtils.initShaderProgram(BaseGame.defaultShader, BaseGame.waveShader)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        if (BaseGame.enableCustomShaders) {
            try {
                drawWithShader(batch, parentAlpha)
            } catch (error: Throwable) {
                super.draw(batch, parentAlpha)
            }
        } else {
            super.draw(batch, parentAlpha)
        }
    }

    override fun act(dt: Float) {
        super.act(dt)
        time += dt
    }

    private fun drawWithShader(batch: Batch, parentAlpha: Float) {
        batch.shader = shaderProgram
        shaderProgram.setUniformf("u_time", time)
        shaderProgram.setUniformf("u_imageSize", Vector2(width, height))
        shaderProgram.setUniformf("u_amplitude", Vector2(amplitudeX, amplitudeY))
        shaderProgram.setUniformf("u_wavelength", Vector2(waveLengthX, waveLengthY))
        shaderProgram.setUniformf("u_velocity", Vector2(velocityX, velocityY))
        super.draw(batch, parentAlpha)
        batch.shader = null
    }
}
