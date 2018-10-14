package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

class EnemyShooter(override var loc: Point3, private val game: Game): Enemy {
    override fun isIn(p: Point): Boolean = p.x > loc.p.x && p.y > loc.p.y && p.x < loc.p.x+radius && p.y < loc.p.y-radius

    private val arbval = 20f //arbitrary value, defining how fast it moves
    private val timeSpentOnScreen = 200f //arbitrary value change later (make divisible by 5)
    private var timeRemainingOnScreen = timeSpentOnScreen
    private var movex = 0 /*if 0, no movement, if 1, positive movement, if -1, negative movement*/
    private var movey = 0 /*if 0, no movement, if 1, positive movement, if -1, negative movement*/
    override fun move(){
        if (timeRemainingOnScreen == timeSpentOnScreen){
            when (loc.p.x){
                0f -> movex = 1
                game.view.eyeSize.x -> movex = -1
                else -> {
                    when(loc.p.y){
                        0f -> movey = 1
                        game.view.eyeSize.y -> movey = -1
                        else -> {
                            println("EnemyShooter spawned in bad location")
                        }
                    }
                }
            }
        } else if (timeRemainingOnScreen >= (timeSpentOnScreen*4/5)){
            when(movex){
                1 -> loc.p.x + game.speed/arbval
                -1 -> loc.p.x - game.speed/arbval
                else -> {
                    when(movey){
                        1 -> loc.p.y + game.speed/arbval
                        -1 -> loc.p.y + game.speed/arbval
                        else -> println("EnemyShooter not moving good")
                    }
                }
            }
        } else if (timeRemainingOnScreen == (timeSpentOnScreen*3/5)|| timeRemainingOnScreen == (timeSpentOnScreen*(2/5))) {
            game.nonShipList.add(EnemyParticle(loc, game.shipList[(Math.random()*game.shipList.size).toInt()].loc, game))
        } else if (timeRemainingOnScreen == (timeSpentOnScreen/5)){
            movex = movex * -1
            movey = movey * -1
        }
        else if (timeRemainingOnScreen < (timeSpentOnScreen/5)){
            when(movex){
                1 -> loc.p.x + 2f*game.speed/arbval
                -1 -> loc.p.x - 2f*game.speed/arbval
                else -> {
                    when(movey){
                        1 -> loc.p.y + 2f*game.speed/arbval
                        -1 -> loc.p.y - 2f*game.speed/arbval
                        else -> println("EnemyShooter not moving good")
                    }
                }
            }
        }
    }
    private val bitmap: Bitmap

    init {
        val matrix = Matrix()
        //matrix.setRotate(angle-90) //chjange to appropriate thing
        val base = game.loadedResources.shooter
        bitmap = Bitmap.createBitmap(base, 0, 0, base.width, base.height, matrix, false)
    }
    private var radius = (bitmap.height+bitmap.width)/2
    override val canKill
        get() = loc.p.y-radius > game.view.eyeSize.y ||
                loc.p.y < 0 ||
                loc.p.x > game.view.eyeSize.x ||
                loc.p.x < 0
    override fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}