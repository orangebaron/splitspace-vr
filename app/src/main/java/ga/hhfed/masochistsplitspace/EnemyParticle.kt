package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import kotlin.math.atan

class EnemyParticle(override var loc: Point3, shiploc: Point3, private val game: Game): Enemy{
    override fun isIn (p: Point): Boolean = p.x > loc.p.x && p.y > loc.p.y && p.x < loc.p.x+radius && p.y < loc.p.y+radius
    private val speed = ((shiploc.p-loc.p)/(shiploc.p-loc.p).size)*100f
    override fun move() {
        loc = Point3(loc.p+speed*game.oneOverFps,loc.eye)
    }

    private val bitmap: Bitmap
    override var streamid: Int = game.loadedResources.thunderstormyMetal
    init {
        if (!canKill)  { streamid = game.loadedResources.playSound(streamid, 1f)}
        val matrix = Matrix()
        matrix.setRotate((Math.atan2(speed.y.toDouble(),speed.x.toDouble())*(180f/3.14159265)).toFloat()) //change to appropriate thing
        val base = game.view.loadedResources.flame
        bitmap = Bitmap.createBitmap(base, 0, 0, base.width, base.height, matrix, false)
    }
    private val radius = (bitmap.height+bitmap.width)/2

    override val canKill
        get() = loc.p.y-radius > game.view.eyeSize.y ||
                loc.p.y < 0 ||
                loc.p.x > game.view.eyeSize.x ||
                loc.p.x < 0

    override fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}