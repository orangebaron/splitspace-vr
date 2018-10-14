package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint


class Ship(var loc: Point3, private val skin: Bitmap, var speed: Point, private val game: Game) { /*add game*/
    var speedMultiplier = 1f
    fun move() {
        if ((loc.p.x>game.view.eyeSize.x || loc.p.x<0f) && speed.x*loc.p.x>0) speed = Point(-speed.x,speed.y)
        if ((loc.p.y>game.view.eyeSize.y || loc.p.y<0f) && speed.y*loc.p.y>0) speed = Point(speed.x,-speed.y)
        speed += Point(game.view.tiltManager.LRsteerAngle,-game.view.tiltManager.nodAngle.toFloat()*30)*game.oneOverFps*speedMultiplier
        if (speed.size>20f*speedMultiplier) speed = speed*(20f*speedMultiplier)/speed.size
        loc.p += speed * game.speed //create appropriate move thing
    }
    var canKill = false
        //get() = field || loc.p.y > game.view.eyeSize.y || loc.p.x < 0f || loc.p.x > game.view.eyeSize.x
        //private set

    fun explode() {
        canKill = true
        //game.runSafelyThreaded("(Ship)addExplosion",3){game.addExplosion(loc)} //note this
    }

    /*private val shipPaint = Paint()*/
    private val bitmap: Bitmap
    init {
        val matrix = Matrix()
        //matrix.setRotate(Math.toDegrees(Math.atan(xDirection.toDouble())).toFloat()) //XDIRECTION WEE OOO WEE OOO
        bitmap = Bitmap.createBitmap(skin,0,0, skin.width, skin.height, matrix,false)
    }

    private val paint = Paint()
    fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}