package com.example.rmcontrol

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmcontrol.databinding.ActivityMainBinding

class BtListActivity : AppCompatActivity(), RcAdapter.Listener {

    private var btAdapter:BluetoothAdapter? = null
    private lateinit var binding: ActivityMainBinding
    private  lateinit var adapter: RcAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bt = findViewById<TextView>(R.id.bt)

        init()
    }

    private fun init() {
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager.adapter
        adapter = RcAdapter(this)
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter
        getPairedDevices()

    }



    private fun getPairedDevices() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT),1)
            }
        }
        val bt = findViewById<TextView>(R.id.bt)
        val tempList = ArrayList<ListItem>()
        var pairedDev: Set<BluetoothDevice>? = btAdapter?.bondedDevices
        pairedDev?.forEach{
            //Log.d("MyLog", "Name ${it.name}")
            //val text = bt.text.toString()
            //bt.text = it.name + "\n" + text
            tempList.add(ListItem(it.name, it.address))
        }

        adapter.submitList(tempList)
    }

    companion object {
        const val DEVICE_KEY = "devive_key"
    }

    override fun onClick(item: ListItem) {
        val i = Intent().apply {
            putExtra(DEVICE_KEY, item)
        }

        setResult(RESULT_OK, i)
        finish()
    }
}