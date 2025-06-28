package com.example.rmcontrol

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import java.io.IOException
import java.util.*


@SuppressLint("MissingPermission")
class ConnectThread(private val device:BluetoothDevice) : Thread() {

    val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    var mSocket: BluetoothSocket? = null
    lateinit var rThread:ReciveThread

    private var isPermission = false

    init {

        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        }catch (i: IOException){

        }
    }


    override fun run() {
        try {
            Log.d("MyLog", "connecting...")
            mSocket?.connect()
            Log.d("MyLog", "connected!")
            rThread = ReciveThread(mSocket!!)
            rThread.start()
        }catch (i: IOException){
            Log.d("MyLog", "no connect")
            closeConnection()
        }
    }


    fun closeConnection(){
        try {
            mSocket?.close()
        }catch (i: IOException){

        }
    }


}