package ga.hhfed.masochistsplitspace

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_GRAVITY
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL

class TiltManager(activity: MainActivity) {
    init {
        val manager = activity.getSystemService(SENSOR_SERVICE) as SensorManager
        val listener = object: SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
            override fun onSensorChanged(p0: SensorEvent?) {}
        }
        manager.registerListener(listener,manager.getDefaultSensor(TYPE_GRAVITY), SENSOR_DELAY_NORMAL)
    }
}