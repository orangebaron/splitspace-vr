package ga.hhfed.masochistsplitspace

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class LoadedResources(res: Resources) {
    val shipTest: Bitmap = BitmapFactory.decodeResource(res,R.drawable.pinkship)
    val shooter: Bitmap = BitmapFactory.decodeResource(res,R.drawable.flamedisciple)
    val flame: Bitmap = BitmapFactory.decodeResource(res,R.drawable.firefly)
}