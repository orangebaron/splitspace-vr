package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import kotlin.math.atan

class EnemyParticle(override var loc: Point3, val shiploc: Point3, private val game: Game): Enemy{
    override fun isIn (p: Point): Boolean = false//p.x > loc.p.x && p.y > loc.p.y && p.x < loc.p.x+radius && p.y < loc.p.y-radius
    private val yintercept = loc.p.y
    private val speedMultiplier = 10f
    override fun move() {
        loc = Point3(Point(loc.p.x + game.speed*speedMultiplier,yintercept + loc.p.x * slope*speedMultiplier),loc.eye)
    }
    private val slope = ((shiploc.p.y.toDouble()-loc.p.y.toDouble())/(shiploc.p.x.toDouble()-loc.p.x.toDouble())).toFloat()

    private val bitmap: Bitmap
    init {
        val matrix = Matrix()
        matrix.setRotate((atan(slope)*(180f/3.14159265)).toFloat()) //change to appropriate thing
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