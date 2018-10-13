package ga.hhfed.masochistsplitspace

import android.graphics.*
import java.util.*
import kotlin.concurrent.timerTask

class Game(fps: Int, val loadedResources: LoadedResources, val view: VRView){
    private val oneOverFps = 1f/fps
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
    val mainPowerup = Powerup.Split //TODO:set this later or something
    val sidePowerup: Powerup? = null

    var shipList = mutableListOf(Ship(Point3(Point(0f,0f),VRView.Eye.Left),view.loadedResources.shipTest,Point(5f,5f), this))
    var nonShipList = mutableListOf<ExtraObject>()
    var countdownVariable = .5f

    private fun addRandomObject() {
        nonShipList.add(when {
            (Math.random()<1) -> EnemySineMan(Point3(
                    if (Math.random()<.25) Point(0f,Math.random().toFloat()*view.eyeSize.y)
                    else if (Math.random()<.25) Point(view.eyeSize.x,Math.random().toFloat()*view.eyeSize.y)
                    else if (Math.random()<.25) Point(Math.random().toFloat()*view.eyeSize.x,0f)
                    else Point(Math.random().toFloat()*view.eyeSize.x,view.eyeSize.y)
                    ,if(Math.random()<.5)VRView.Eye.Left else VRView.Eye.Right),this)
            else -> error("this should not appear")
        })
    }

    private fun tick() {
        countdownVariable -= oneOverFps/5
        if (countdownVariable<0) {
            countdownVariable = .5f
            addRandomObject()
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
        view.invalidate()
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