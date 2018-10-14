package ga.hhfed.masochistsplitspace

import android.graphics.Canvas

class PowerupPill(override val loc: Point3, private val powerup: Game.Powerup, private val game: Game): ExtraObject {
    override fun isIn(p: Point): Boolean = p.x > loc.p.x && p.y > loc.p.y && p.x < loc.p.x+bitmap.height && p.y < loc.p.y+bitmap.width
    override fun move(){}
    private val bitmap = game.loadedResources.flame //TODO pill thing
    override var canKill = false
    override fun touchEffect(ship: Ship) {
        game.activatePowerup(powerup)
        canKill = true
    }
    override fun draw(canvas: Canvas) {
        game.view.drawBmpForEye(canvas, bitmap, loc)
    }
}