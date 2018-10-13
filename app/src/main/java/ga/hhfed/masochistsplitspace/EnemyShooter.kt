package ga.hhfed.masochistsplitspace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

class EnemyShooter(override var loc: Point3, private val game: Game): Enemy {
    override fun isIn(p: Point): Boolean { TODO("not implemented") }
    override var canKill = false
        private set
    private val arbval = 20 //arbitrary value, defining how fast it moves
    private val timeSpentOnScreen = 100; //arbitrary value; change later (make divisible by 5)
    private var timeRemainingOnScreen = timeSpentOnScreen;
    private var movex = 0 /*if 0, no movement, if 1, positive movement, if -1, negative movement*/
    private var movey = 0 /*if 0, no movement, if 1, positive movement, if -1, negative movement*/
    override fun move(){
        if (timeRemainingOnScreen == timeSpentOnScreen){
            when (loc.p.x){
                0f -> movex = 1;
                game.view.eyeSize.x -> movex = -1;
                else -> {
                    when(loc.p.y){
                        0f -> movey = 1;
                        game.view.eyeSize.y -> movey = -1;
                        else -> {
                            println("EnemyShooter spawned in bad location");
                        }
                    }
                }
            }
        } else if (timeRemainingOnScreen >= (timeSpentOnScreen*(4/5))){
            when(movex){
                1 -> loc.p.x + game.speed/arbval;
                -1 -> loc.p.x - game.speed/arbval;
                else -> {
                    when(movey){
                        1 -> loc.p.y + game.speed/arbval;
                        -1 -> loc.p.y + game.speed/arbval;
                        else -> println("EnemeyShooter not moving good");
                    }
                }
            }
        } else if (timeRemainingOnScreen == (timeSpentOnScreen*(3/5))|| timeRemainingOnScreen == (timeSpentOnScreen*(2/5))) {
            EnemyParticle(loc, game.shipList[0].loc, game); //remember to create shiploc var in game
        } else if (timeRemainingOnScreen == (timeSpentOnScreen/5)){
            movex = movex * -1;
            movey = movey * -1;
        }
        else if (timeRemainingOnScreen < (timeSpentOnScreen/5)){
            when(movex){
                1 -> loc.p.x + game.speed/arbval;
                -1 -> loc.p.x - game.speed/arbval;
                else -> {
                    when(movey){
                        1 -> loc.p.y + game.speed/arbval;
                        -1 -> loc.p.y + game.speed/arbval;
                        else -> println("EnemeyShooter not moving good");
                    }
                }
            }
        } else {
            canKill = true
        }
    }
    private val bitmap: Bitmap
    init {
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