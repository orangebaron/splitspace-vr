package ga.hhfed.masochistsplitspace

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build

class LoadedResources(private val res: Resources, private val view: VRView) {
    private fun scaledBmp(r: Int, size: Float): Bitmap {
        val base = BitmapFactory.decodeResource(res, r)
        return Bitmap.createScaledBitmap(base, (size * view.eyeSize.x).toInt(), (size * view.eyeSize.x * base.height / base.width).toInt(), false)
    }

    val shipTest: Bitmap = scaledBmp(R.drawable.pinkship, 1 / 20f)
    val shooter: Bitmap = scaledBmp(R.drawable.flamedisciple, 1 / 10f)
    val flame: Bitmap = scaledBmp(R.drawable.firefly, 1 / 20f)
    val signMan: Bitmap = scaledBmp(R.drawable.sineman, 1 / 10f)
    val lineMan: Bitmap = scaledBmp(R.drawable.lineman, 1/20f)

    fun playSound(soundID: Int, volume: Float, rate: Float = 1f, loop: Int = 0) {
        sounds.play(soundID, volume, volume, 0, loop, rate)
    }
    val sounds: SoundPool =
            if (Build.VERSION.SDK_INT >= 21)
                SoundPool.Builder().setMaxStreams(20).build()
            else
                @Suppress("deprecation")
                SoundPool(20, AudioManager.STREAM_MUSIC, 0)
    val boomSound = sounds.load(view.context, R.raw.boom1, 0)
    val boomSound2 = sounds.load(view.context, R.raw.boom2, 0)
    val boomSound3 = sounds.load(view.context, R.raw.boom4, 0)
    val signManNoise = sounds.load(view.context, R.raw.signman, 0)
    val lineManNoise = sounds.load(view.context, R.raw.eyeball_man, 0)
    val shooterNoises = sounds.load(view.context, R.raw.voice007, 0)
    val laserNoises = sounds.load(view.context, R.raw.pew, 0)
    val moistNoises = sounds.load(view.context, R.raw.moist_noises, 0);
    val thunderstormyMetal = sounds.load(view.context, R.raw.thunderstormy_metal, 0);

    //private val soundtracks: List<MediaPlayer> = listOf(
            /*MediaPlayer.create(context, R.raw.splitspace_theme),
            MediaPlayer.create(context, R.raw.perilous_battle),
            MediaPlayer.create(context, R.raw.strange_battle))*/
    private var playing = 2
    private var paused = true
    /*fun playTrack(id: Int) {
        if (playing == id) return
        playing = id
        if (paused) resumeTrack()
        soundtracks.forEach {
            if (it == soundtracks[id])
                it.start()
            else {
                it.stop()
                it.prepare()
            }
        }
    }
    fun pauseTrack() {
        paused = true
        soundtracks[playing].pause()
    }
    fun resumeTrack() {
        paused = false
        soundtracks[playing].start()
    }
    init {
        soundtracks.forEach {
            it.setVolume(.5f, .5f)
            it.isLooping = true
        }
        resumeTrack()
    }*/
}