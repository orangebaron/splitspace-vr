package ga.hhfed.masochistsplitspace

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class LoadedResources(private val res: Resources, private val view: VRView) {
    private fun scaledBmp(r: Int, size: Float): Bitmap {
        val base = BitmapFactory.decodeResource(res, r)
        return Bitmap.createScaledBitmap(base, (size * view.eyeSize.y).toInt(), (size * view.eyeSize.x *base.height/base.width).toInt(), false)
    }
    val shipTest: Bitmap = scaledBmp(R.drawable.pinkship,1/20f)
    val shooter: Bitmap = scaledBmp(R.drawable.flamedisciple,1/10f)
    val flame: Bitmap = scaledBmp(R.drawable.firefly,1/10f)
}