package ga.hhfed.masochistsplitspace

import android.graphics.*

class Ship(var loc: Point, private val skin: Bitmap, private val game: Game) { /*add game*/
    /*private val timeBetweenParticles = game.fps/5
    private var timeToNextParticle = timeBetweenParticles*/
    fun move() {
        //loc += Point(xDirection, 0f) * game.speed //create appropriate move thing
    }
    var canKill = false
        get() = field || loc.y > game.view.eyeSize.y || loc.x < 0f || loc.x > game.view.eyeSize.x
        private set

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
        canvas.drawBitmap(bitmap, loc.x - bitmap.width/2, loc.y - bitmap.height/2, paint)
    }


}