package ga.hhfed.masochistsplitspace

import android.app.Activity
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.timerTask

class Game(val fps: Int, val loadedResources: LoadedResources, val view: VRView){
    //render, and tick
    var speed = 5/fps //initialize please
    val oneOverFps = 1f/fps

    enum class Powerup {
        Split, // Splits
        Ghost, // Temporary Invulnerability (needs them GRAPHICS)
        Laser, // Shoots a LASER
        Agility, // Increases mobility (side to side non-jumping motion)
    }
    enum class GameDefinitions{
        AddObj, // add enemy or object specified by number
        RemoveObj,
        ChangeSpeed, // change speed; the associated number is multiplied by size.y/(fps*100) to get the real value
    }

    var shipList = mutableListOf(Ship(Point(0f,0f),view.loadedResources.shipTest,this))
    var nonShipList = mutableListOf<ExtraObject>()
    private var objectsToSpawn = MutableList(7){false}
    var countdownVariable = .5f

    private fun addRandomObject() {
        //val rad = ((Math.random()*.07 + .035)*view.eyeSize.x).toFloat()
        //nonShipList.add(when {
            /*(objectsToSpawn[6] && Math.random()<.01) ->
                IceDisciple(Point((Math.random() * view.size.x).toFloat(), yOffset), this)

            (objectsToSpawn[5] && Math.random()<.01) ->
                FlameDisciple(Point((Math.random() * view.size.x).toFloat(), yOffset), this)
            (objectsToSpawn[4] && Math.random()<.1) ->
                EnemySatellite(rad, Math.random().toFloat()*2+1, Point(if (Math.random()>.5) 0f else view.size.x, -rad + yOffset), this)

            (objectsToSpawn[3] && Math.random()<.1 && status != GameStatus.BeforeNormal) ->
                EnemyPinkAsteroid(rad, Point((Math.random() * view.size.x).toFloat(), -rad + yOffset), this)

            (objectsToSpawn[2] && Math.random()<.07) ->
                FuelCan(Math.random()<.03, Point((Math.random() * view.size.x).toFloat(), -rad + yOffset), this)

            (objectsToSpawn[1] && Math.random()<.2) ->
                Coin(Math.random()<.03, Point((Math.random() * view.size.x).toFloat(), -rad + yOffset), this)

            objectsToSpawn[0] ->
                EnemyShooter(rad, Point((Math.random() * view.size.x).toFloat(), -rad + yOffset), this)

            else ->
                BlankExtraObject()*/
        //})
    }

    fun tick() {
        countdownVariable -= oneOverFps/5
        if (countdownVariable<0) {
            countdownVariable = .5f
            addRandomObject()
        }
    }
    fun draw(canv: Canvas) {
        shipList.forEach { it.draw(canv) }
        nonShipList.forEach { it.draw(canv) }
    }


    val timer = Timer()
    init {
        timer.scheduleAtFixedRate(timerTask{ tick() }, (1000 * oneOverFps).toLong(), (1000 * oneOverFps).toLong())
    }

}