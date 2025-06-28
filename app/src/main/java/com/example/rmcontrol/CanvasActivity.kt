package com.example.rmcontrol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class CanvasActivity : AppCompatActivity() {

    var start:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)

        var t1 = findViewById<TextView>(R.id.t1)
        var t2 = findViewById<TextView>(R.id.t2)
        var t3 = findViewById<TextView>(R.id.t3)

        val customDrawableView = findViewById<CustomDrawableView>(R.id.customDrawableView)

        Thread {
            start = true
            while (start) {
                Thread.sleep(10)
                runOnUiThread {
                    t1.text = customDrawableView.anglFirst.toString()
                    t2.text = customDrawableView.angleF.toString()
                    t3.text = customDrawableView.angleF2.toString()
                }
            }
        }.start()
    }
}