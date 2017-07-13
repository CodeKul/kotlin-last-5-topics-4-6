package com.codekul.telephony

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val rec = object : BroadcastReceiver() {
        override fun onReceive(con: Context?, intent: Intent?) {
            if(intent?.action === "com.codekul.sent") {
                Toast.makeText(con, "Sent", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(con, "Delivered", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        Log.i("@codekul", "Imei - " + mgr.deviceId)
        Log.i("@codekul", "Phone Num - " + mgr.line1Number)
        Log.i("@codekul", "Nw Operator - " + mgr.networkOperatorName)
        Log.i("@codekul", "Sim Operator - " + mgr.simOperatorName)

        val fil = IntentFilter()
        fil.addAction("com.codekul.sent")
        fil.addAction("com.codekul.delivered")

        registerReceiver(rec, fil)

        btnSend.setOnClickListener {
            val smsMgr = SmsManager.getDefault()
            smsMgr.sendTextMessage(
                    "9552070065",
                    null,
                    "Hello frm codekul",
                    PendingIntent.getBroadcast(
                            this,
                            1234,
                            Intent("com.codekul.sent"),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    ),
                    PendingIntent.getBroadcast(
                            this,
                            1235,
                            Intent("com.codekul.delivered"),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    )
            )
        }
    }
}
