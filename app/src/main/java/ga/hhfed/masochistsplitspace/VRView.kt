package ga.hhfed.masochistsplitspace

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class VRView: View {
    private val activity: Context

    constructor(context: Context): super(context) { activity = context }
    constructor(context: Context,attributeSet: AttributeSet): super(context,attributeSet) { activity = context }
    constructor(context: Context,attributeSet: AttributeSet,int: Int): super(context,attributeSet,int) { activity = context }

    override fun onDraw(canv: Canvas?) {
        super.onDraw(canv)
    }
}