package com.example.rmcontrol

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ReciveThread(val bSocket:BluetoothSocket) : Thread() {

    var inStream:InputStream? = null
    var outStream:OutputStream? = null

    init {
        try {
            inStream = bSocket.inputStream
        } catch (i : IOException) {

        }

        try {
            outStream = bSocket.outputStream
        } catch (i : IOException) {

        }
    }

    override fun run() {
        val buf = ByteArray(256)
        while (true) {
            try {
                val size = inStream?.read(buf)
                val message = String(buf, 0, size!!)
                Log.d("MyLog", "Message: $message")
            }catch (i:IOException) {
                break
            }
        }
    }

    fun sendMessage(number:String) {
        //val byteArray = ByteArray(1) // Число Int занимает 4 байта

        val stringToSend = number.toString() + '\n' // преобразуем число в строку
        val byteArray = stringToSend.toByteArray(Charsets.UTF_8) // преобразуем строку в массив байтов

        //byteArray[0] = number.toByte()
        try {
            outStream?.write(byteArray)

        } catch (i : IOException) {

        }
    }

    fun sendMessage(byteArray: ByteArray) {
        try {
            outStream?.write(byteArray)
        } catch (i : IOException) {

        }
    }
}