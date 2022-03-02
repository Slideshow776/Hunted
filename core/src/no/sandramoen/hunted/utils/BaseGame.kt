package no.sandramoen.hunted.utils

import com.badlogic.gdx.*
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Cursor
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter
import java.util.*
import kotlin.system.measureTimeMillis

abstract class BaseGame(var googlePlayServices: GooglePlayServices?, appLocale: String) : Game(), AssetErrorListener {
    private val tag = "BaseGame.kt"
    private val appLocale = appLocale

    init { game = this }

    companion object {
        private var game: BaseGame? = null

        lateinit var assetManager: AssetManager
        lateinit var fontGenerator: FreeTypeFontGenerator
        const val WORLD_WIDTH = 100f
        const val WORLD_HEIGHT = WORLD_WIDTH
        const val scale = 1.0f
        var RATIO = 0f
        val lightPink = Color(1f, .816f, .94f, 1f)
        val lightBrown = Color(0.859f, 0.788f, 0.706f, 1f)
        val lightYellowBrown = Color(0.969f, 0.812f, 0.569f, 1f)
        var enableCustomShaders = true // debugging purposes

        // game assets
        var gps: GooglePlayServices? = null
        var smallLabelStyle: LabelStyle? = null
        var bigLabelStyle: LabelStyle? = null
        var textButtonStyle: TextButtonStyle? = null
        var textureAtlas: TextureAtlas? = null
        var skin: Skin? = null
        var defaultShader: String? = null
        var waveShader: String? = null
        var shockwaveShader: String? = null
        var shotSound: Sound? = null
        var swooshSound: Sound? = null
        var netFireSound: Sound? = null
        var netCaughtSound: Sound? = null
        var hornSound: Sound? = null
        var heartBeatSlowerSound: Sound? = null
        var heartBeatFasterSound: Sound? = null
        var pantingFadeInSound: Sound? = null
        var pantingFadeOutSound: Sound? = null
        var bushSound: Sound? = null
        var caughtVoiceSound: Sound? = null
        var horn_blowVoiceSound: Sound? = null
        var level1_intro2VoiceSound: Sound? = null
        var runVoiceSound: Sound? = null
        var level_middleVoiceSound: Sound? = null
        var level1_intro1VoiceSound: Sound? = null
        var clickSound: Sound? = null
        var hoverOverEnterSound: Sound? = null
        var ambient1Music: Music? = null
        var level1Music: Music? = null
        var level2Music: Music? = null

        // game state
        var prefs: Preferences? = null
        var loadPersonalParameters = false
        var soundVolume = .75f
        var musicVolume = .5f
        var isGPS = false
        var currentLocale: String? = null
        var myBundle: I18NBundle? = null

        fun setActiveScreen(screen: BaseScreen) {
            screen.initialize()
            game?.setScreen(screen)
        }
    }

    override fun create() {
        Gdx.input.setCatchKey(Keys.BACK, true) // so that android doesn't exit game on back button
        Gdx.input.inputProcessor = InputMultiplexer() // discrete input

        // global variables
        gps = this.googlePlayServices
        GameUtils.loadGameState()
        if (!loadPersonalParameters) {
            currentLocale = appLocale
            soundVolume = .75f
            musicVolume = .25f
            if (Gdx.app.type == Application.ApplicationType.Android)
                isGPS = true
        }
        RATIO = Gdx.graphics.width.toFloat() / Gdx.graphics.height
        try {
            skin = Skin(Gdx.files.internal("skins/default/uiskin.json"))
        } catch (error: Throwable) {
            Gdx.app.error(tag, "Error: Could not load skin: $error")
        }

        // asset manager
        val time = measureTimeMillis {
            assetManager = AssetManager()
            assetManager.setErrorListener(this)
            assetManager.load("images/included/packed/hunted.pack.atlas", TextureAtlas::class.java)

            // music
            assetManager.load("audio/music/577446__klankbeeld__park-may-720pm-nl-denoise-210523-0284.wav", Music::class.java)
            assetManager.load("audio/music/415274__neolein__mystic-ambience.wav", Music::class.java)
            assetManager.load("audio/music/437182__neolein__electronicas-voice.wav", Music::class.java)

            // sounds
            assetManager.load("audio/sound/Laser_Shoot28.wav", Sound::class.java)
            assetManager.load("audio/sound/Hit_Hurt13.wav", Sound::class.java)
            assetManager.load("audio/sound/Laser_Shoot42.wav", Sound::class.java)
            assetManager.load("audio/sound/Explosion9.wav", Sound::class.java)
            assetManager.load("audio/sound/218488__danmitch3ll__distant-horns.wav", Sound::class.java)
            assetManager.load("audio/sound/heartBeatSlower.wav", Sound::class.java)
            assetManager.load("audio/sound/heartBeatFaster.wav", Sound::class.java)
            assetManager.load("audio/sound/pantingFadeIn.wav", Sound::class.java)
            assetManager.load("audio/sound/pantingFadeOut.wav", Sound::class.java)
            assetManager.load("audio/sound/bushesCracking.wav", Sound::class.java)
            assetManager.load("audio/sound/click1.wav", Sound::class.java)
            assetManager.load("audio/sound/hoverOverEnter.wav", Sound::class.java)

            assetManager.load("audio/sound/story/caught.wav", Sound::class.java)
            assetManager.load("audio/sound/story/do you hear that.wav", Sound::class.java)
            assetManager.load("audio/sound/story/find the hunter.wav", Sound::class.java)
            assetManager.load("audio/sound/story/run!.wav", Sound::class.java)
            assetManager.load("audio/sound/story/still out there.wav", Sound::class.java)
            assetManager.load("audio/sound/story/you're being hunted.wav", Sound::class.java)

            // fonts
            val resolver = InternalFileHandleResolver()
            assetManager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
            assetManager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))
            assetManager.setLoader(Text::class.java, TextLoader(InternalFileHandleResolver()))

            // skins
            // assetManager.load("skins/arcade/arcade.json", Skin::class.java)

            // i18n
            assetManager.load("i18n/MyBundle", I18NBundle::class.java, I18NBundleParameter(Locale(currentLocale)))

            // shaders
            assetManager.load(AssetDescriptor("shaders/default.vs", Text::class.java, TextLoader.TextParameter()))
            assetManager.load(AssetDescriptor("shaders/wave.fs", Text::class.java, TextLoader.TextParameter()))
            assetManager.load(AssetDescriptor("shaders/shockwave.fs", Text::class.java, TextLoader.TextParameter()))

            assetManager.finishLoading()

            textureAtlas = assetManager.get("images/included/packed/hunted.pack.atlas") // all images are found in this global static variable

            // audio
            ambient1Music = assetManager.get("audio/music/577446__klankbeeld__park-may-720pm-nl-denoise-210523-0284.wav", Music::class.java)
            level1Music = assetManager.get("audio/music/415274__neolein__mystic-ambience.wav", Music::class.java)
            level2Music = assetManager.get("audio/music/437182__neolein__electronicas-voice.wav", Music::class.java)

            shotSound = assetManager.get("audio/sound/Laser_Shoot28.wav", Sound::class.java)
            swooshSound = assetManager.get("audio/sound/Hit_Hurt13.wav", Sound::class.java)
            netFireSound = assetManager.get("audio/sound/Laser_Shoot42.wav", Sound::class.java)
            netCaughtSound = assetManager.get("audio/sound/Explosion9.wav", Sound::class.java)
            hornSound = assetManager.get("audio/sound/218488__danmitch3ll__distant-horns.wav", Sound::class.java)
            heartBeatSlowerSound = assetManager.get("audio/sound/heartBeatSlower.wav", Sound::class.java)
            heartBeatFasterSound = assetManager.get("audio/sound/heartBeatFaster.wav", Sound::class.java)
            pantingFadeInSound = assetManager.get("audio/sound/pantingFadeIn.wav", Sound::class.java)
            pantingFadeOutSound = assetManager.get("audio/sound/pantingFadeOut.wav", Sound::class.java)
            bushSound = assetManager.get("audio/sound/bushesCracking.wav", Sound::class.java)
            clickSound = assetManager.get("audio/sound/click1.wav", Sound::class.java)
            hoverOverEnterSound = assetManager.get("audio/sound/hoverOverEnter.wav", Sound::class.java)

            caughtVoiceSound = assetManager.get("audio/sound/story/caught.wav", Sound::class.java)
            horn_blowVoiceSound = assetManager.get("audio/sound/story/do you hear that.wav", Sound::class.java)
            level1_intro2VoiceSound = assetManager.get("audio/sound/story/find the hunter.wav", Sound::class.java)
            runVoiceSound = assetManager.get("audio/sound/story/run!.wav", Sound::class.java)
            level_middleVoiceSound = assetManager.get("audio/sound/story/still out there.wav", Sound::class.java)
            level1_intro1VoiceSound = assetManager.get("audio/sound/story/you're being hunted.wav", Sound::class.java)

            // deathLSound = assetManager.get("audio/sound/BNB_SFX_DEATH_L.wav", Sound::class.java)

            // text files
            defaultShader = assetManager.get("shaders/default.vs", Text::class.java).getString()
            waveShader = assetManager.get("shaders/wave.fs", Text::class.java).getString()
            shockwaveShader = assetManager.get("shaders/shockwave.fs", Text::class.java).getString()

            // skin
            // skin = assetManager.get("skins/arcade/arcade.json", Skin::class.java)

            // i18n
            myBundle = assetManager["i18n/MyBundle", I18NBundle::class.java]

            // fonts
            FreeTypeFontGenerator.setMaxTextureSize(2048) // solves font bug that won't show some characters like "." and "," in android
            fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("fonts/hemi-head-426.rg-bolditalic.ttf"))
            val fontParameters = FreeTypeFontParameter()
            fontParameters.size = (.038f * Gdx.graphics.height).toInt() // Font size is based on width of screen...
            fontParameters.color = Color.WHITE
            fontParameters.borderWidth = 2f
            fontParameters.shadowColor = Color(0f, 0f, 0f, .25f)
            fontParameters.shadowOffsetX = 2
            fontParameters.shadowOffsetY = 2
            fontParameters.borderColor = Color.BLACK
            fontParameters.borderStraight = true
            fontParameters.minFilter = TextureFilter.Linear
            fontParameters.magFilter = TextureFilter.Linear
            val fontSmall = fontGenerator.generateFont(fontParameters)
            fontParameters.size = (.2f * Gdx.graphics.height).toInt() // Font size is based on width of screen...
            val fontBig = fontGenerator.generateFont(fontParameters)

            val buttonFontParameters = FreeTypeFontParameter()
            buttonFontParameters.size = (.08f * Gdx.graphics.height).toInt() // If the resolutions height is 1440 then the font size becomes 86
            buttonFontParameters.color = Color.WHITE
            buttonFontParameters.borderWidth = 2f
            buttonFontParameters.borderColor = Color.BLACK
            buttonFontParameters.borderStraight = true
            buttonFontParameters.minFilter = TextureFilter.Linear
            buttonFontParameters.magFilter = TextureFilter.Linear
            val buttonCustomFont = fontGenerator.generateFont(buttonFontParameters)

            smallLabelStyle = LabelStyle()
            smallLabelStyle!!.font = fontSmall
            bigLabelStyle = LabelStyle()
            bigLabelStyle!!.font = fontBig

            textButtonStyle = TextButtonStyle()
            textButtonStyle!!.font = buttonCustomFont
            textButtonStyle!!.fontColor = Color.WHITE

            if (Gdx.app.type == Application.ApplicationType.Desktop) {
                val customCursor: Cursor = Gdx.graphics.newCursor(Pixmap(Gdx.files.internal("images/excluded/cursor.png")), 0, 0)
                Gdx.graphics.setCursor(customCursor)
            }
        }
        Gdx.app.error(tag, "Asset manager took $time ms to load all game assets.")
    }

    override fun dispose() {
        super.dispose()
        GameUtils.saveGameState()
        if (gps != null) gps!!.signOut()
        try { // TODO: uncomment this when development is done
            assetManager.dispose()
            fontGenerator.dispose()
        } catch (error: UninitializedPropertyAccessException) {
            Gdx.app.error("BaseGame", "$error")
        }
    }

    override fun error(asset: AssetDescriptor<*>, throwable: Throwable) {
        Gdx.app.error(tag, "Could not load asset: " + asset.fileName, throwable)
    }
}
