package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import kotlin.math.atan

class EnemyShooter(override var loc: Point3, private val game: Game): Enemy {
    override fun isIn(p: Point): Boolean = p.x > loc.p.x && p.y > loc.p.y && p.x < loc.p.x+radius && p.y < loc.p.y+radius

    private val arbval = .05f //arbitrary value, defining how fast it moves
    private val timeSpentOnScreen = 40f //arbitrary value change later (make divisible by 5)
    private var timeRemainingOnScreen = timeSpentOnScreen
    private var movex = 0 /*if 0, no movement, if 1, positive movement, if -1, negative movement*/
    private var movey = 0 /*if 0, no movement, if 1, positive movement, if -1, negative movement*/
    private var doneOnce = false
    private var doneNone = true
    override fun move(){

        if (timeRemainingOnScreen == timeSpentOnScreen){
            when (loc.p.x){
                0f -> movex = 1
                game.view.eyeSize.x -> movex = -1
                else -> {
                    when(loc.p.y){
                        0f -> movey = 1
                        game.view.eyeSize.y -> movey = -1
                        else -> println("UWE-EnemyShooter spawned in bad location")
                    }
                }
            }
        } else if (timeRemainingOnScreen >= (timeSpentOnScreen*2f/3f)){
            when(movex){
                1 -> loc = Point3(Point(loc.p.x + game.speed/arbval, loc.p.y),loc.eye)
                -1 -> loc = Point3(Point(loc.p.x - game.speed/arbval, loc.p.y),loc.eye)
                else -> {
                    when(movey){
                        1 -> loc = Point3(Point(loc.p.x, loc.p.y + game.speed/arbval),loc.eye)
                        -1 -> loc = Point3(Point(loc.p.x, loc.p.y - game.speed/arbval),loc.eye)
                        else -> println("UWE-EnemyShooter not moving good")
                    }
                }
            }
        } else if (doneNone && timeRemainingOnScreen>=(timeSpentOnScreen*2f/3.5f)) {
            doneNone = false
            doneOnce = true
            game.addToNonShipList.add(EnemyParticle(loc, game.shipList[(Math.random()*game.shipList.size).toInt()].loc, game))

        } else if (doneOnce &&timeRemainingOnScreen>=(timeSpentOnScreen*2f/4.5f)){
            doneOnce = false
            game.addToNonShipList.add(EnemyParticle(loc, game.shipList[(Math.random()*game.shipList.size).toInt()].loc, game))

        } else if (timeRemainingOnScreen <timeSpentOnScreen/3 && timeRemainingOnScreen > 0) {
            when(movex) {
                1 -> loc = Point3(Point(loc.p.x - game.speed/arbval, loc.p.y),loc.eye)
                -1 -> loc = Point3(Point(loc.p.x + game.speed/arbval, loc.p.y),loc.eye)
                else -> {
                    when(movey){
                        1 -> loc = Point3(Point(loc.p.x, loc.p.y - game.speed/arbval),loc.eye)
                        -1 -> loc = Point3(Point(loc.p.x, loc.p.y + game.speed/arbval),loc.eye)
                        else -> println("EnemyShooter not moving good")
                    }
                }
            }
        } else {
            canKill = true
            game.loadedResources.sounds.pause(streamid)
        }
        timeRemainingOnScreen -= game.speed
        //matrix.setRotate((atan(slope)*(180f/3.14159265)).toFloat()) //chjange to appropriate thing
    }
    private val bitmap: Bitmap
    private var slope = (game.shipList[0].loc.p.y - loc.p.y)/(game.shipList[0].loc.p.x  - loc.p.x)
    val matrix = Matrix()
    override var streamid: Int = game.loadedResources.shooterNoises
    override var canKill = false
    init {
        if (!canKill)  { streamid = game.loadedResources.playSound(streamid, 1f)}
        //matrix.setRotate((atan(slope)*(180f/3.14159265)).toFloat()) //chjange to appropriate thing
        val base = game.loadedResources.shooter
        bitmap = Bitmap.createBitmap(base, 0, 0, base.width, base.height, matrix, false)
    }

    private val radius = (bitmap.width+bitmap.height)/2

    override fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}