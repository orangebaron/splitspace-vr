package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import kotlin.math.atan

class EnemyLineMan(override var loc: Point3, private val game: Game): Enemy{
    override fun isIn(p: Point): Boolean = p.x > loc.p.x && p.y > loc.p.y && p.x < loc.p.x+radius && p.y < loc.p.y+radius
    override val canKill
        get() = loc.p.y-radius > game.view.eyeSize.y ||
                loc.p.y < 0 ||
                loc.p.x > game.view.eyeSize.x ||
                loc.p.x < 0
    override fun move(){
        loc = Point3(Point(loc.p.x + game.speed, yintercept + loc.p.y + game.speed*slope.toFloat()),loc.eye)
    }
    private val yintercept = loc.p.y
    private val slope = (Math.random()*2 - 1)*2
    private val angle = atan(slope).toFloat()
    private val bitmap: Bitmap
    init {
        val matrix = Matrix()
        matrix.setRotate((angle*(180f/3.14159265)).toFloat()) //chjange to appropriate thing
        val base = game.loadedResources.lineMan //TODO MAKE LINEMAN GRAPHIC
        bitmap = Bitmap.createBitmap(base, 0, 0, base.width, base.height, matrix, false)
    }
    private val radius = (bitmap.height+bitmap.width)/2

    override fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}