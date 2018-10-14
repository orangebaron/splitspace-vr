package ga.hhfed.masochistsplitspace

import android.graphics.*


class Ship(var loc: Point3, var speed: Point, private val game: Game) { /*add game*/
    private fun recalculateBitmap() {
        timeUntilSwitchAngle = .05f
        val matrix = Matrix()
        matrix.setRotate(Math.toDegrees(Math.atan2(speed.y.toDouble(),speed.x.toDouble())).toFloat()+180) //XDIRECTION WEE OOO WEE OOO

        val base = if(game.ghostTimer > 0) game.loadedResources.ghost else
            (if(game.agilityTimer > 0) game.loadedResources.agility else game.loadedResources.shipTest) //TODO AGILITY KID
        bitmap = Bitmap.createBitmap(base,0,0, base.width, base.height, matrix,false)
    }
    private var timeUntilSwitchAngle = 0f
    fun move() {
        speed += Point(game.view.tiltManager.LRsteerAngle,-game.view.tiltManager.nodAngle.toFloat()*30)*game.oneOverFps*speedMultiplier
        if (speed.size>20f*speedMultiplier) speed = speed*(20f*speedMultiplier)/speed.size
        if ((loc.p.x>game.view.eyeSize.x || loc.p.x<0f) && speed.x*loc.p.x>0) speed = Point(-speed.x,speed.y)
        if ((loc.p.y>game.view.eyeSize.y || loc.p.y<0f) && speed.y*loc.p.y>0) speed = Point(speed.x,-speed.y)
        loc = Point3(loc.p+(speed * game.speed*1.5f),loc.eye)  //create appropriate move thing
        timeUntilSwitchAngle-=game.oneOverFps
        if (timeUntilSwitchAngle<=0) recalculateBitmap()
    }
    var speedMultiplier = 1f
    var canKill = false
        //get() = field || loc.p.y > game.view.eyeSize.y || loc.p.x < 0f || loc.p.x > game.view.eyeSize.x
        private set

    fun explode() {
        if (!(game.ghostTimer > 0)) {
            canKill = true
            game.loadedResources.playSound(streamid, 1f)
        }
        //game.runSafelyThreaded("(Ship)addExplosion",3){game.addExplosion(loc)} //note this
    }

    /*private val shipPaint = Paint()*/
    var streamid = when {
        (Math.random() < .5) -> game.loadedResources.deathnoise1
        (Math.random() <= 1) -> game.loadedResources.deathnoise2
        else -> error("bad error")
    }

        private lateinit var bitmap: Bitmap
        init {
            recalculateBitmap()
        }

    fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}

