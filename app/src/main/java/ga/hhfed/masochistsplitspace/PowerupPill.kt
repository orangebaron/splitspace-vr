package ga.hhfed.masochistsplitspace

import android.graphics.Canvas

class PowerupPill(override val loc: Point3, private val powerup: Game.Powerup, private val game: Game): ExtraObject {
    override fun isIn(p: Point): Boolean = p.x > loc.p.x-bitmap.height && p.y > loc.p.y-bitmap.width && p.x < loc.p.x+2*bitmap.height && p.y < loc.p.y+2*bitmap.width
    override fun move(){}
    private val bitmap = when (powerup) {
        Game.Powerup.Split -> game.loadedResources.redPill
        Game.Powerup.Agility -> game.loadedResources.greenPill
        Game.Powerup.Ghost -> game.loadedResources.bluePill
    }
    override var canKill = false
    override var streamid: Int = -1
    override fun touchEffect(ship: Ship) {
        game.activatePowerup(powerup)
        canKill = true
    }
    override fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}