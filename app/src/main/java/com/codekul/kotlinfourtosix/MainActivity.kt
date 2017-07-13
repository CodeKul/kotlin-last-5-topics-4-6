package com.codekul.kotlinfourtosix

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = BluetoothAdapter.getDefaultAdapter()
        Log.i("@codekul", """ Address - ${adapter.address} Name - ${adapter.name} """)

        btnEnable.setOnClickListener {
            startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }

        btnDiscoverable.setOnClickListener {
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            startActivity(discoverableIntent)
        }

        btnPaired.setOnClickListener {
            adapter?.bondedDevices?.forEach {
                Log.i(
                        "@codekul",
                        """ name is ${it.name} and address is ${it.address} """
                )
            }
        }

        btnServer.setOnClickListener {

            Thread {
                val bss = adapter.listenUsingRfcommWithServiceRecord(
                        "chat-service",
                        UUID.fromString("00001000-0000-1000-8000-00805f9b34fb")
                )
                val bs = bss.accept()
                val dos = DataOutputStream(bs.outputStream)
                dos.writeUTF("Android kotlin codekul")

            }.start()
        }

        btnClient.setOnClickListener {

            Thread {
                val remote = adapter.getRemoteDevice("")
                val bs = remote.createRfcommSocketToServiceRecord(
                        UUID.fromString("00001000-0000-1000-8000-00805f9b34fb")
                )
                bs.connect()
                val dis = DataInputStream(bs.inputStream)
                Log.i("@codekul", "Data Is "+dis.readUTF())
                dis.close()
            }.start()
        }
    }
}
