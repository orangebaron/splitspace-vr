package ga.hhfed.masochistsplitspace

import android.graphics.*
import java.util.*
import kotlin.concurrent.timerTask

class Game(fps: Int, val loadedResources: LoadedResources, val view: VRView){
    val oneOverFps = 1f/fps
    var speed = 5*oneOverFps


    enum class Powerup {
        Split, // Splits
        Ghost, // Temporary Invulnerability (needs them GRAPHICS)
        Laser, // Shoots a LASER
        Agility // Increases mobility (side to side non-jumping motion)
    }
    enum class GameDefinitions{
        AddObj, // add enemy or object specified by number
        RemoveObj,
        ChangeSpeed, // change speed; the associated number is multiplied by size.y/(fps*100) to get the real value
    }
    var mainPowerup = Powerup.Split //TODO:set this later or something
    var mainPowerAct = false
    var mainPowerCooldown = 0f
    val sidePowerup: Powerup? = null
    val sidePowerAct = false
    var ghostTimer = 0f
    var agilityTimer = 0f

    var shipList = mutableListOf(Ship(Point3(Point(0f,0f),VRView.Eye.Left),view.loadedResources.shipTest,Point(5f,5f), this))
    var nonShipList = mutableListOf<ExtraObject>()
    var addToNonShipList = mutableListOf<ExtraObject>()
    var countdownVariable = .5f

    private fun addRandomObject() {
        nonShipList.add(when {
            (Math.random()<.25) -> EnemySineMan(edgeSpawning,this)
            (Math.random()<.50) -> EnemyLineMan(edgeSpawning, this)
            (Math.random()<=1) -> EnemyShooter(edgeSpawning, this)
            else -> error("this should not appear")
        })
    }

    private val edgeSpawning
        get() = Point3(
            if (Math.random()<.25) Point(0f,Math.random().toFloat()*view.eyeSize.y)
            else if (Math.random()<.25) Point(view.eyeSize.x,Math.random().toFloat()*view.eyeSize.y)
            else if (Math.random()<.25) Point(Math.random().toFloat()*view.eyeSize.x,0f)
            else Point(Math.random().toFloat()*view.eyeSize.x,view.eyeSize.y)
                ,if(Math.random()<.5)VRView.Eye.Left else VRView.Eye.Right)

    private data class MotionState(var lookingUp: Boolean=false, var lookingDown: Boolean=false, var turningRight: Boolean=false, var turningLeft: Boolean=false)
    private var lastMotionState = MotionState()

    private fun checkifPowerup(): Boolean{
        if (mainPowerAct && (mainPowerCooldown==0f)){
            mainPowerCooldown = 1000000f
            return true
        }
        return false
    }
    private fun tick() {
        println("UWE-tick")
        countdownVariable -= oneOverFps / 5
        if (countdownVariable < 0) {
            countdownVariable = .5f
            addRandomObject()
        }

        if (ghostTimer > 0f) {
            shipList.forEach {
                it.canKill = true
            }
            ghostTimer -= this.speed
        }

        if (agilityTimer == 0f) {
            shipList.forEach {
                it.speedMultiplier = 1f
            }
        }

        val extraObjectsToRemove = mutableListOf<ExtraObject>()
        nonShipList.forEach {
            it.move()
            shipList.forEach { ship -> if (it.isIn(ship.loc.p) && it.loc.eye == ship.loc.eye) it.touchEffect(ship) }
            if (it.canKill)
                extraObjectsToRemove.add(it)
        }
        extraObjectsToRemove.forEach { nonShipList.remove(it) }

        val shipsToRemove = mutableListOf<Ship>()
        shipList.forEach {
            if (it.canKill)
                shipsToRemove.add(it)
            else
                it.move()
        }
        shipsToRemove.forEach { shipList.remove(it) }

        for(i in 0 until addToNonShipList.size){
            nonShipList.add(addToNonShipList[i])
        } //Doesn't really make sense but it will do; TODO fix this
        addToNonShipList.clear()
        //MOTION STUFF
        val currentMotionState = MotionState(
                lookingUp = view.tiltManager.nodAngle > .35,
                lookingDown = view.tiltManager.nodAngle < -.35,
                turningRight = view.tiltManager.LRturnSpeed > 2,
                turningLeft = view.tiltManager.LRturnSpeed < -2
        )
        if (currentMotionState != lastMotionState) {
            //println("UWE- $currentMotionState $lastMotionState")
            if (currentMotionState.turningRight && shipList[0].loc.eye == VRView.Eye.Left) {
                shipList.forEach { it.loc.eye = VRView.Eye.Right }
            } else if (currentMotionState.turningLeft && shipList[0].loc.eye == VRView.Eye.Right) {
                shipList.forEach { it.loc.eye = VRView.Eye.Left }
            }
            lastMotionState = currentMotionState
        }

        if (currentMotionState.lookingUp)
            mainPowerAct = true

        when (mainPowerup) {
            Powerup.Split -> {
                if (checkifPowerup()) {
                    println("UWE splitting")
                    for (i in 0 until (shipList.size)) {
                        shipList.add(Ship(Point3(shipList[i].loc.p, shipList[i].loc.eye), view.loadedResources.shipTest, Point(shipList[i].speed.x * (-1f), shipList[i].speed.y), this))
                    }
                }//some pop up saying something along those lines
            }
            Powerup.Ghost -> {
                if (checkifPowerup()) {
                    //change ship's bitmap
                    ghostTimer = 20f
                }    //some pop up saying something along those lines
            }
            Powerup.Laser -> {
                if (checkifPowerup()) {
                    //do
                }   //some pop up saying something along those lines
            }
            Powerup.Agility -> {
                if (checkifPowerup()) {
                    shipList.forEach {
                        it.speedMultiplier = 1.5f
                    }
                    agilityTimer = 40f
                }  //some pop up saying something along those lines
            }
        }
        mainPowerAct = false
        view.invalidate()
    }

    fun draw(canv: Canvas) {
        println("UWE-draw nonShipListsize: ${nonShipList.size}")
        println("UWE-draw ShipListsize: ${shipList.size}")
        try {
            shipList.forEach { it.draw(canv) }
            nonShipList.forEach { it.draw(canv) }
        } catch (e: Throwable) {
            println("UWE-ERR drawing $e")
        }
    }



    val timer = Timer()
    init {
        println("UWE-init")
        timer.scheduleAtFixedRate(timerTask {
            try {
                tick()
            } catch (e: Throwable) {
                println("UWE-ERR timer $e")
            }
        }, (1000 * oneOverFps).toLong(), (1000 * oneOverFps).toLong())
    }


}