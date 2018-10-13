package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

class EnemyLineMan(override var loc: Point3, private val game: Game): Enemy{
    override fun isIn(p: Point): Boolean = p.x > loc.p.x && p.y > loc.p.y && p.x < loc.p.x+radius && p.y < loc.p.y-radius
    override var canKill = false //todo set die command or autodone?

    override fun move(){
        loc.p = Point(loc.p.x + game.speed, yintercept + loc.p.y + game.speed*slope.toFloat())
    }
    private val yintercept = loc.p.y
    private val slope = (Math.random()*2 - 1)*5
    private val bitmap: Bitmap
    init {
        val matrix = Matrix()
        //matrix.setRotate(angle-90) //chjange to appropriate thing
        val base = game.loadedResources.shooter //TODO MAKE LINEMAN GRAPHIC
        bitmap = Bitmap.createBitmap(base, 0, 0, base.width, base.height, matrix, false)
    }
    private val radius = (bitmap.height+bitmap.width)/2

    override fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}