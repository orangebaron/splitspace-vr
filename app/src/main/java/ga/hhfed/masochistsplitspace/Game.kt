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

    private val edgeSpawning //mm jarry I approve of ur getter
        get() = Point3(
            if (Math.random()<.25) Point(0f,Math.random().toFloat()*view.eyeSize.y)
            else if (Math.random()<.25) Point(view.eyeSize.x,Math.random().toFloat()*view.eyeSize.y)
            else if (Math.random()<.25) Point(Math.random().toFloat()*view.eyeSize.x,0f)
            else Point(Math.random().toFloat()*view.eyeSize.x,view.eyeSize.y)
                ,if(Math.random()<.5)VRView.Eye.Left else VRView.Eye.Right)

    fun activatePowerup(p: Powerup) {
        when (p) {
            Powerup.Split ->
                for (i in 0 until (shipList.size)) {
                    shipList.add(Ship(Point3(shipList[i].loc.p, shipList[i].loc.eye), view.loadedResources.shipTest, Point(shipList[i].speed.x * (-1f), shipList[i].speed.y), this))
                }
            Powerup.Ghost ->
                //TODO: change ship's bitmap
                ghostTimer = 20f
            Powerup.Laser -> {
                //do
            }
            Powerup.Agility -> {
                shipList.forEach {
                    it.speedMultiplier = 1.5f
                }
                agilityTimer = 40f
            }
        }
    }

    private fun tick() {
        countdownVariable -= oneOverFps / 5
        if (countdownVariable < 0) {
            countdownVariable = .5f
            addRandomObject()
        }

        if (ghostTimer > 0f) {
            ghostTimer -= this.oneOverFps
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
            if (it.canKill && ghostTimer <= 0f)
                shipsToRemove.add(it)
            else
                it.move()
        }
        shipsToRemove.forEach { shipList.remove(it) }

        for(i in 0 until addToNonShipList.size){
            nonShipList.add(addToNonShipList[i])
        }
        addToNonShipList.clear()

        //MOTION STUFF
        if (view.tiltManager.LRturnSpeed > 2 && shipList[0].loc.eye == VRView.Eye.Left) {
            shipList.forEach { it.loc.eye = VRView.Eye.Right }
        } else if (view.tiltManager.LRturnSpeed < -2 && shipList[0].loc.eye == VRView.Eye.Right) {
            shipList.forEach { it.loc.eye = VRView.Eye.Left }
        }

        view.invalidate()
    }

    fun draw(canv: Canvas) {
        try {
            shipList.forEach { it.draw(canv) }
            nonShipList.forEach { it.draw(canv) }
        } catch (e: Throwable) {
            println("UWE-ERR drawing $e")
        }
    }

    val timer = Timer()
    init {
        timer.scheduleAtFixedRate(timerTask {
            try {
                tick()
            } catch (e: Throwable) {
                println("UWE-ERR timer $e")
            }
        }, (1000 * oneOverFps).toLong(), (1000 * oneOverFps).toLong())
    }
}