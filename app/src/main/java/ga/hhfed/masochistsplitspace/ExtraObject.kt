package ga.hhfed.masochistsplitspace

import android.graphics.Canvas



//RENDERING AAAAAAAAAAAH

interface ExtraObject {
    val canKill: Boolean
    fun isIn(p: Point): Boolean
    fun touchEffect(ship: Ship)
    fun move()
    fun draw(canvas: Canvas)
    val loc: Point3
}