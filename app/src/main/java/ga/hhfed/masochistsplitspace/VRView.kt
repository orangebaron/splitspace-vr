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
        canv?.let { canvas ->
            drawBmpForEye(canvas, loadedResources.shipTest, Eye.Left, Point(300f, 400f))
            drawBmpForEye(canvas, loadedResources.shipTest, Eye.Right, Point(300f, 400f))
        }
        systemUiVisibility = barsHidden
    }

    val eyeWidth: Point
        get() = Point(height / 2f, width.toFloat())

    enum class Eye { Left, Right }

    fun drawForEye(eye: Eye, points: List<Point>, func: (List<Point>) -> Unit) {
        func(points.map {
            if (eye == Eye.Left)
                Point(width - it.y, it.x)
            else
                Point(width - it.y, it.x + height / 2)
        })
    }

    fun drawBmpForEye(canv: Canvas, bmp: Bitmap, eye: Eye, point: Point) = drawForEye(eye, listOf(point)) {
        canv.drawBitmap(bmp, it[0].x, it[0].y, bmpPaint)
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