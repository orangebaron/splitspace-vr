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
        Agility // Increases mobility (side to side non-jumping motion)
    }
    var ghostTimer = 0f
    var agilityTimer = 0f

    var shipList = mutableListOf(Ship(Point3(Point(0f,0f),VRView.Eye.Left),Point(5f,5f), this))
    var nonShipList = mutableListOf<ExtraObject>()
    var addToNonShipList = mutableListOf<ExtraObject>()
    var countdownVariable = .5f

    private fun addRandomObject() {
        nonShipList.add(when {
            (Math.random()<.125) -> EnemyShooter(edgeSpawning, this)
            (Math.random()<.5) -> EnemySineMan(edgeSpawning,this)
            else -> EnemyLineMan(edgeSpawning, this)
        })
    }
    val pillEye: VRView.Eye
        get() = randoEye()
    private fun randoEye (): VRView.Eye{
        return when{
            (Math.random()<.5) -> VRView.Eye.Left
            (Math.random()<=1) -> VRView.Eye.Right
            else -> error("should be impossibru Uwe-error")
        }
    }
    private fun addPill(){
        nonShipList.add(when {
                    (Math.random()<.3333) -> PowerupPill(Point3(Point((view.eyeSize.x/3f + Math.random()*view.eyeSize.x/3f).toFloat(), (view.eyeSize.y/3f + Math.random()*view.eyeSize.y/3f).toFloat()), pillEye), Powerup.Split, this)
            (Math.random()<.5) -> PowerupPill(Point3(Point((view.eyeSize.x/3f + Math.random()*view.eyeSize.x/3f).toFloat(), (view.eyeSize.y/3f + Math.random()*view.eyeSize.y/3f).toFloat()), pillEye), Powerup.Ghost, this)
            (Math.random()<1) -> PowerupPill(Point3(Point((view.eyeSize.x/3f + Math.random()*view.eyeSize.x/3f).toFloat(), (view.eyeSize.y/3f + Math.random()*view.eyeSize.y/3f).toFloat()), pillEye), Powerup.Agility, this)
            else -> error("should be impossibru Uwe-error")
        })
        pillExists = true
    }

    private val edgeSpawning //mm jarry I approve of ur getter
        get() = Point3(
            if (Math.random()<.25) Point(0f,Math.random().toFloat()*view.eyeSize.y)
            else if (Math.random()<.25) Point(view.eyeSize.x,Math.random().toFloat()*view.eyeSize.y)
            else if (Math.random()<.25) Point(Math.random().toFloat()*view.eyeSize.x,0f)
            else Point(Math.random().toFloat()*view.eyeSize.x,view.eyeSize.y)
                ,if(Math.random()<.5)VRView.Eye.Left else VRView.Eye.Right)

    fun activatePowerup(p: Powerup) {
        pillExists = false
        when (p) {
            Powerup.Split ->
                for (i in 0 until (shipList.size)) {
                    shipList.add(Ship(Point3(shipList[i].loc.p, shipList[i].loc.eye), Point(shipList[i].speed.x * (-1f)+(if (shipList[i].speed.x>0) -10 else 10), shipList[i].speed.y), this))
                }
            Powerup.Ghost ->
                ghostTimer = 15f
            Powerup.Agility -> {
                shipList.forEach {
                    it.speedMultiplier = 1.5f
                }
                agilityTimer = 20f
            }
        }
    }

    var pillcountdownVariable = 0f
    private var pillExists = false
    private fun tick() {
        if (shipList.size == 0) {
            if(view.tiltManager.LRsteerAngle>6 || view.tiltManager.LRsteerAngle<-6) {
                shipList = mutableListOf(Ship(Point3(Point(0f, 0f), VRView.Eye.Left), Point(5f, 5f), this))
                nonShipList = mutableListOf()
                pillExists = false
            }
            view.invalidate()
            return
        }

        countdownVariable -= oneOverFps
        if (countdownVariable < 0) {
            countdownVariable = .5f
            addRandomObject()
        }
        if (!pillExists) pillcountdownVariable -= oneOverFps
        if(pillcountdownVariable <= 0 && !pillExists){
            println("let there be pill")
            pillcountdownVariable = 10f
            addPill()
            pillExists = true
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
            if (it.canKill) {
                try {if(it.streamid>0) {println("sound should die here streamid: " + it.streamid); this.loadedResources.sounds.pause(it.streamid)}}
                catch (e:Throwable) {(println("Maybe error $e"))}
                extraObjectsToRemove.add(it)
            }
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

        if (addToNonShipList.size > 0) {
            for (i in 0 until addToNonShipList.size) {
                nonShipList.add(addToNonShipList[i])
            }
            addToNonShipList = mutableListOf();
        }

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
            if (shipList.size == 0) {
                val img = loadedResources.gameOverScreen
                view.drawBmpForEye(canv,img,Point3((view.eyeSize-Point(img.height.toFloat(),img.width.toFloat()))*.5f,VRView.Eye.Left))
                view.drawBmpForEye(canv,img,Point3((view.eyeSize-Point(img.height.toFloat(),img.width.toFloat()))*.5f,VRView.Eye.Right))
            }
        } catch (e: Throwable) {
            println("UWE-ERR drawing $e")
        }
    }

    val timer = Timer()
    init {
        println("jry speed: " + speed)
        timer.scheduleAtFixedRate(timerTask {
            try {
                tick()
            } catch (e: Throwable) {
                println("UWE-ERR timer $e")
            }
        }, (1000 * oneOverFps).toLong(), (1000 * oneOverFps).toLong())
    }
}