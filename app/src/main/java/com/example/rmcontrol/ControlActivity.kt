package com.example.rmcontrol

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.rmcontrol.databinding.ActivityControlBinding


class ControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityControlBinding
    private lateinit var actListLauncher: ActivityResultLauncher<Intent>
    lateinit var btConnection:BtConnection
    private var listItem:ListItem? = null

    var start = false
    var startThread = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBtListResult()
        init()

        binding.apply {
            /*bA.setOnClickListener {
                btConnection.sendMessage("A")
            }
            bB.setOnClickListener {
                btConnection.sendMessage("B")
            }*/

            btS1.setOnTouchListener(RepeatListener(40 * 2, 10*2, View.OnClickListener {
                // код, который нужно выполнить повторно
                //Log.d("MyLog", "ewew")
                btConnection.sendMessage("G")
            }))

            btS2.setOnTouchListener(RepeatListener(40*2, 10*2, View.OnClickListener {
                // код, который нужно выполнить повторно
                //Log.d("MyLog", "ewew")
                btConnection.sendMessage("H")
            }))


            btGrip1.setOnTouchListener(RepeatListener(40 * 2, 10*2, View.OnClickListener {
                // код, который нужно выполнить повторно
                //Log.d("MyLog", "ewew")
                btConnection.sendMessage("I")
            }))

            btGrip2.setOnTouchListener(RepeatListener(40*2, 10*2, View.OnClickListener {
                // код, который нужно выполнить повторно
                //Log.d("MyLog", "ewew")
                btConnection.sendMessage("J")
            }))


            testBt.setOnTouchListener(RepeatListener(40 * 2, 10*2, View.OnClickListener {
                // код, который нужно выполнить повторно
                //Log.d("MyLog", "ewew")
                btConnection.sendMessage("C")
            }))

            testBt2.setOnTouchListener(RepeatListener(40*2, 10*2, View.OnClickListener {
                // код, который нужно выполнить повторно
                //Log.d("MyLog", "ewew")
                btConnection.sendMessage("D")
            }))

            btM1.setOnTouchListener(RepeatListener(40 * 2, 10*2, View.OnClickListener {
                // код, который нужно выполнить повторно
                //Log.d("MyLog", "ewew")
                btConnection.sendMessage("E")
            }))

            btM12.setOnTouchListener(RepeatListener(40*2, 10*2, View.OnClickListener {
                // код, который нужно выполнить повторно
                //Log.d("MyLog", "ewew")
                btConnection.sendMessage("F")
            }))

            bA.setOnTouchListener(RepeatListener(40 * 2, 10*2, View.OnClickListener { //40 10
                btConnection.sendMessage("A")

                /*Thread {
                    startThread = true
                    while (startThread) {
                        Thread.sleep(100)
                        if(start)
                        {

                            val receivedValue = DataHolder.angle
                            val receivedValue2 = DataHolder.angle2
                            val receivedValue3 = DataHolder.angle3
                            btConnection.sendMessage2(receivedValue.toString() + " " + receivedValue2.toString() + " " + receivedValue3.toString())
                            //btConnection.sendMessage(receivedValue2.toString())

                        }

                    }
                }.start()*/
            }))

            bB.setOnTouchListener(RepeatListener(40*2, 10*2, View.OnClickListener {
                btConnection.sendMessage("B")
                Log.d("MyLog", "ewew")



                /*Thread {
                    startThread = true
                    while (startThread) {
                        Thread.sleep(100)
                        if(start)
                        {

                            val receivedValue = DataHolder.angle
                            val receivedValue2 = DataHolder.angle2
                            val receivedValue3 = DataHolder.angle3
                            btConnection.sendMessage2(receivedValue.toString() + " " + receivedValue2.toString() + " " + receivedValue3.toString())
                            //btConnection.sendMessage(receivedValue2.toString())

                        }

                    }
                }.start()*/
            }))

        }

    }

    private fun repeatAction() {
        Log.d("MyLog", "yes")
    }

    private fun init() {
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        btConnection = BtConnection(btAdapter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.control_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.id_list) {
            actListLauncher.launch(Intent(this, BtListActivity::class.java))
        } else if(item.itemId == R.id.id_content) {
            listItem.let {
                btConnection.connect(it?.mac!!)
                start = true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onBtListResult() {
        actListLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK) {
                //Log.d("MyLog", "Name: ${(it.data?.getSerializableExtra(BtListActivity.DEVICE_KEY) as ListItem).name}")
                listItem =  it.data?.getSerializableExtra(BtListActivity.DEVICE_KEY) as ListItem
            }
        }
    }

    fun onClickGoCanvas(view: View) {
        btConnection.sendMessage("Z")
        Thread {
            startThread = true
            while (startThread) {
                Thread.sleep(100)
                if(start)
                {

                    val receivedValue = DataHolder.angle
                    val receivedValue2 = DataHolder.angle2
                    val receivedValue3 = DataHolder.angle3
                    val receivedValue4 = DataHolder.rotateAngle
                    btConnection.sendMessage2(receivedValue.toString() + " " + receivedValue2.toString() + " " + receivedValue3.toString() + " " + receivedValue4.toString())
                    //btConnection.sendMessage(receivedValue2.toString())

                }

            }
        }.start()

        val intent = Intent(this, CanvasActivity::class.java)
        startActivity(intent)
    }

    fun onClickGoOpencv(view: View) {
        val intent = Intent(this, OpencvActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        startThread = false
        start = false
    }

}