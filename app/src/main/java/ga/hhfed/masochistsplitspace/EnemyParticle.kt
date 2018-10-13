package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

class EnemyParticle(override var loc: Point3, val shiploc: Point3, private val game: Game): Enemy{
    override fun isIn(p: Point): Boolean { TODO("not implemented") }
    private val yintercept = loc.p.y;
    override fun move() {
        loc.p = Point(loc.p.x + game.speed,yintercept + loc.p.x * slope)
    }
    private val bitmap: Bitmap
    init {
        val matrix = Matrix()
        //matrix.setRotate(angle-90) //change to appropriate thing
        val base = game.view.loadedResources.flame
        bitmap = Bitmap.createBitmap(base, 0, 0, base.width, base.height, matrix, false)
    }
    private val radius = (bitmap.height+bitmap.width)/2
    private val slope = ((shiploc.p.y.toDouble()-loc.p.y.toDouble())/(shiploc.p.x.toDouble()-loc.p.x.toDouble())).toFloat();

    override val canKill
        get() = loc.p.y-radius > game.view.eyeSize.y ||
                loc.p.y < 0 ||
                loc.p.x > game.view.eyeSize.x ||
                loc.p.x < 0

    override fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}