package com.codekul.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mgr.getSensorList(Sensor.TYPE_ALL).forEach { Log.i("@codekul", " Name is ${it.name}") }

        accln(mgr)

    }

    fun light(mgr: SensorManager) {

        val listener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }

            override fun onSensorChanged(event: SensorEvent?) {
                txtInfo.text = "" + event!!.values[0]
            }
        }
        mgr.registerListener(
                listener,
                mgr.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun accln(mgr: SensorManager) {

        val listener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }

            override fun onSensorChanged(event: SensorEvent?) {
                val dt = "X - " + event!!.values[0] + " Y - " + event.values[1] + " Z - " + event.values[2]
                txtInfo.text = dt
            }
        }
        mgr.registerListener(
                listener,
                mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
        )
    }
}
