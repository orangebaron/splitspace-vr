package ga.hhfed.masochistsplitspace

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View

class VRView: View {
    private val activity: Context
    val loadedResources = LoadedResources(resources)

    constructor(context: Context) : super(context) {
        activity = context
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        activity = context
    }

    constructor(context: Context, attributeSet: AttributeSet, int: Int) : super(context, attributeSet, int) {
        activity = context
    }

    private val bmpPaint = Paint()
    override fun onDraw(canv: Canvas?) {
        super.onDraw(canv)
        /*canv?.let { canvas ->
            for (x in listOf(0f,eyeSize.x/2-loadedResources.shipTest.height/2,eyeSize.x-loadedResources.shipTest.height))
                for (y in listOf(0f,eyeSize.y/2-loadedResources.shipTest.width/2,eyeSize.x-loadedResources.shipTest.width))
                    for (eye in listOf(Eye.Left,Eye.Right))
                        drawBmpForEye(canvas,loadedResources.shipTest,Point3(Point(x,y),eye))
            }*/
        canv?.let(game::draw)
        systemUiVisibility = barsHidden
    }

    val eyeSize: Point
        get() = Point(height/2f - 350, width.toFloat()-250)

    enum class Eye { Left, Right }

    val game = Game(60,LoadedResources(resources),this)

    fun drawForEye(points: List<Point3>, func: (List<Point3>) -> Unit) {
        func(points.map {
            Point3(
                if (it.eye == Eye.Left)
                    Point(width - it.p.y - 120, it.p.x + 227)
                else
                    Point(width - it.p.y - 120, it.p.x + 145 + height / 2)
            , it.eye)
        })
    }

    fun drawBmpForEye(canv: Canvas, bmp: Bitmap, point: Point3) = drawForEye(listOf(point)) {
        canv.drawBitmap(bmp, it[0].p.x-bmp.height, it[0].p.y, bmpPaint)
    }

    //can add more draw functions if necessary

    private val barsHidden =
            (if (Build.VERSION.SDK_INT >= 19)
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            else 0) or
                    (if (Build.VERSION.SDK_INT >= 16)
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_FULLSCREEN
                    else 0) or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    init {
        systemUiVisibility = barsHidden
    }
}