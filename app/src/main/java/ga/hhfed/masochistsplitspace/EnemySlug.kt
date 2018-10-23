package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

class EnemySlug(override var loc: Point3, private val game: Game): Enemy{
    override fun isIn(p: Point): Boolean = p.x > loc.p.x && p.y > loc.p.y && p.x < loc.p.x+radius && p.y < loc.p.y+radius
    override var canKill = false
        private set

    private val accelConst = .01 // SUBJECT TO CHANGE
    private val startConst = .25 // SUBJECT TO CHANGE

    private var xspeed = (game.shipList[0].loc.p.x.toDouble()-loc.p.x.toDouble())*startConst
    private var yspeed = (game.shipList[0].loc.p.y.toDouble()-loc.p.y.toDouble())*startConst

    private fun accel (){
        yspeed += (game.shipList[0].loc.p.y.toDouble()-loc.p.y.toDouble())*accelConst
        xspeed += (game.shipList[0].loc.p.x.toDouble()-loc.p.x.toDouble())*accelConst
    }

    //ABOVE CHANGING SHIPLIST TO CORRECT EYE ADN STUFF IS NEEDED (or doing it at some point)

    override fun move(){
        loc = Point3(Point((loc.p.x + xspeed).toFloat(), (loc.p.y + yspeed).toFloat()),loc.eye)
        accel()
    }
    override var streamid = game.loadedResources.moistNoises

    private val bitmap: Bitmap
    init {
        if (!canKill)  { streamid = game.loadedResources.playSound(streamid, 1f)}
        val matrix = Matrix()
        //matrix.setRotate(angle-90) //chjange to appropriate thing
        val base = game.loadedResources.shooter
        bitmap = Bitmap.createBitmap(base, 0, 0, base.width, base.height, matrix, false)
    }
    private val radius = (bitmap.height+bitmap.width)/2
    override fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}
