package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import kotlin.math.*

class EnemySineMan(override var loc: Point3, private val game: Game): Enemy{
    private val axisAngle = 2*3.14159265*Math.random() //atan2(loc.p.x, loc.p.y)
    //private val slope = (Math.random()*2 - 1)*5
    private val startx = loc.p.x
    private val starty = loc.p.y
    private var ticks = 0f
    private val negOne = -1f
    private val sizeFactor = 60f
    private fun tickup(){
        ticks += game.speed/4f
    }

    override fun move(){
        //loc.p = Point(((loc.p.x+game.speed)*cos(axisAngle) + loc.p.y*sin(axisAngle))*20, 20*sin(((loc.p.x+game.speed)*cos(axisAngle) + loc.p.y*sin(axisAngle))))
        tickup();
        loc.p = Point((((ticks)*cos(axisAngle) + sin(ticks)*sin(axisAngle))*sizeFactor).toFloat() + startx, starty + (sizeFactor*6f/5f)*((-1f)*(ticks)*(sin(axisAngle))+sin(ticks)*cos(axisAngle)).toFloat())
        //loc.p = Point(((loc.p.x+game.speed)*cos(axisAngle) + loc.p.y*sin(axisAngle)), sin((-1)*(loc.p.x+game.speed)*cos(axisAngle) + loc.p.y*sin(axisAngle)))
        when (loc.p.x){
            game.view.eyeSize.x -> canKill = true
            0f -> canKill = true
            else -> {
                when(loc.p.y){
                    game.view.eyeSize.y -> canKill = true
                    0f -> canKill = true
                }
            }
        }
    }


    override fun isIn(p: Point): Boolean = p.x > loc.p.x && p.y > loc.p.y && p.x < loc.p.x+radius && p.y < loc.p.y-radius
    override var canKill = false //todo set die command or autodone?
    private val bitmap: Bitmap
    init {
        val matrix = Matrix()
        //matrix.setRotate(angle-90) //chjange to appropriate thing
        val base = game.loadedResources.shooter //TODO MAKE SINEMAN GRAPHIC
        bitmap = Bitmap.createBitmap(base, 0, 0, base.width, base.height, matrix, false)
    }
    private val radius = (bitmap.height+bitmap.width)/2

    override fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}