package ga.hhfed.masochistsplitspace

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.Sensor.*
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL

class TiltManager(activity: MainActivity) {
    // Variable init
    var nodAngle = 0.0
        private set
    var LRsteerAngle = 0f
        private set
    var LRturnSpeed = 0f
        private set
    init {
        val manager = activity.getSystemService(SENSOR_SERVICE) as SensorManager
        val gravityListener = object: SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
            override fun onSensorChanged(p0: SensorEvent?) { p0?.let {
                nodAngle = Math.acos(Math.sqrt(p0.values[0]*p0.values[0]+p0.values[1]*p0.values[1].toDouble())/9.81)*(if(p0.values[2]>0) -1 else 1)
                LRsteerAngle = p0.values[1]
            }}
        }
        manager.registerListener(gravityListener,manager.getDefaultSensor(TYPE_GRAVITY), SENSOR_DELAY_NORMAL)
        val gyroListener = object: SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
            override fun onSensorChanged(p0: SensorEvent?) { p0?.let {
                LRturnSpeed = -p0.values[0]
            }}
        }
        manager.registerListener(gyroListener,manager.getDefaultSensor(TYPE_GYROSCOPE), SENSOR_DELAY_NORMAL)
    }
}
