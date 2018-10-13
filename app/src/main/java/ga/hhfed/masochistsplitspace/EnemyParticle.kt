package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

class EnemyParticle(override var loc: Point, val shiploc: Point, private val game: Game): Enemy{
    override fun isIn(p: Point): Boolean { TODO("not implemented") }
    private val yintercept = loc.y;
    override fun move() {
        loc = Point(loc.x + game.speed,yintercept + loc.x * slope)
    }
    private val bitmap: Bitmap
    init {
        val matrix = Matrix()
        //matrix.setRotate(angle-90) //change to appropriate thing
        val base = game.view.loadedResources.flame
        bitmap = Bitmap.createBitmap(base, 0, 0, base.width, base.height, matrix, false)
    }
    private val radius = (bitmap.height+bitmap.width)/2
    private val slope = ((shiploc.y.toDouble()-loc.y.toDouble())/(shiploc.x.toDouble()-loc.x.toDouble())).toFloat();
    override fun draw(canvas: Canvas) { TODO("not implemented") }

    override val canKill
        get() = loc.y-radius > game.view.eyeSize.y ||
                loc.y < 0 ||
                loc.x > game.view.eyeSize.x ||
                loc.x < 0
}