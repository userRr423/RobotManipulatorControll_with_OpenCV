package com.example.rmcontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import kotlin.math.abs

class CustomDrawableView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()

    var v = MyMath()
    //var v1 = Vec2(11f,5f)
    //var v2 = Vec2(2f,3f)
    var startX = v.getScreenWidth()/2
    var startY = v.getScreenHeight()/2
    var endX = 0f
    var endY = 0f

    var scaleLenght = 3

    var lenght:Int = 3
    /*var tentacle:Segment? = null
    var tentacle1:Segment? = null
    var tentacle2:Segment? = null*/
    var tentacle = arrayOfNulls<Segment>(lenght)
    val size = floatArrayOf(70f, 60f, 45f)


    var seg = Segment(startX.toFloat(), startY.toFloat(), 80f * scaleLenght, v.convert(-70f))
    var seg2 = Segment(seg, 50f * scaleLenght, v.convert(-20f))
    var base = Vec2(startX.toFloat(), startY.toFloat())


    val mainCircle = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
        radius = 20 + 25
    }
    var radius = 20 + 25 + 20
    var xM = base.x - radius
    var yM = base.y - radius

    var m = true

    var a: Float = 0f
    var b: Float = 0f
    var a2: Float = 0f
    var b2: Float = 0f

    var t1 = findViewById<TextView>(R.id.t1)
    val t2 = findViewById<TextView>(R.id.t2)
    val t3 = findViewById<TextView>(R.id.t3)

    var angleF = 0
    var angleF2 = 0
    var anglFirst = 0

    private var customListener: CustomDrawableViewListener? = null

    var initAction = true

    init {
        tentacle[0] = Segment(300f, 200f, size[0]*scaleLenght, v.convert(10f))
        endX = 0f
        endY = 0f

        //tentacle1 = Segment(tentacle!!, size[1], 0f)
        //tentacle2 = Segment(tentacle1!!, size[2], 0f)

        for(i in 1 until lenght)
        {
            tentacle[i] = Segment(tentacle[i-1]!!, size[i]*scaleLenght, 0f)
        }



    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.GREEN
        paint.strokeWidth = 25f

        //println(v1+v2);
        //canvas.drawLine(startX.toFloat(), startY.toFloat(), endX, endY, paint)

        //рабочий код
        /*seg.update()
        seg.positionA(base)
        seg.draw(canvas, paint)

        seg2.update()
        seg2.draw(canvas, paint)
        seg2.follow(endX, endY)
        seg.follow(seg2.a.x, seg2.a.y)
        seg2.a = seg.b*/

        invalidate()

        var end:Segment = tentacle[lenght-1]!!

        xM = end.b.x
        yM = end.b.y

        //v.isPointInsideCircle(xM, yM, radius.toFloat(), endX, endY) &&
        if(v.isPointInsideCircle(xM, yM, radius.toFloat(), endX, endY) && m) {
            println("Yeees")

            end.follow(endX, endY)
            end.update()
        }



        if(!m) {
            tentacle[2]!!.angle = tentacle[1]!!.angle
        }

        //end.draw(canvas, paint)

        for (i in lenght - 2 downTo 0) {
            tentacle[i]!!.follow(tentacle[i+1]!!)
            if(i == 0) {
                tentacle[i]!!.update(true)
            } else {
                tentacle[i]!!.update()
            }
        }

        if(initAction) {
            tentacle[0]!!.angle = v.convert(-150f)

            tentacle[1]!!.angle = v.convert(-150f)

            tentacle[2]!!.angle = v.convert(-120f)
            initAction = false

            endX = 810f
            endY = 850f

            xM = endX
            yM = endY


            DataHolder.angle = anglFirst
            DataHolder.angle2 = angleF
            DataHolder.angle3 = angleF2

            invalidate()
        }


        tentacle[0]!!.positionA(base)

        val s1 = v.calculateSlope(tentacle[0]!!.a.x, tentacle[0]!!.a.y, tentacle[0]!!.b.x, tentacle[0]!!.b.y)
        val s2 = v.calculateSlope(tentacle[1]!!.a.x, tentacle[1]!!.a.y, tentacle[1]!!.b.x, tentacle[1]!!.b.y)

        var angleDegrees = v.calculateAngleBetweenLines(s1, s2)



        if (angleDegrees > 0) {
            a = angleDegrees
            angleF = (180 - a).toInt()
        }

        if (angleDegrees < 0) {
            b = angleDegrees
            angleF = Math.abs(b).toInt()
        }

        val s12 = v.calculateSlope(tentacle[1]!!.a.x, tentacle[1]!!.a.y, tentacle[1]!!.b.x, tentacle[1]!!.b.y)
        val s22 = v.calculateSlope(tentacle[2]!!.a.x, tentacle[2]!!.a.y, tentacle[2]!!.b.x, tentacle[2]!!.b.y)

        var angleDegrees2 = v.calculateAngleBetweenLines(s12, s22)



        if (angleDegrees2 > 0) {
            a2 = angleDegrees2
            angleF2 = (180 - a2).toInt()
        }

        if (angleDegrees2 < 0) {
            b2 = angleDegrees2
            angleF2 = Math.abs(b2).toInt()
        }

        if (s12.toInt() == s22.toInt()) {
            angleF2 = 180
        }

        if (s1.toInt() == s2.toInt()) {
            angleF = 180
        }

        var c22:Vec2 = tentacle[2]!!.b - tentacle[2]!!.a
        var c12:Vec2 = tentacle[1]!!.b - tentacle[1]!!.a
        var c02:Vec2 = tentacle[0]!!.b - tentacle[0]!!.a

        anglFirst = abs(tentacle[0]!!.degreesAngle.toInt())

        angleF = v.calculateAngleBetweenVectors(c02, c12).toInt()
        angleF2 = v.calculateAngleBetweenVectors(c12, c22).toInt()
        customListener?.onVariableChanged(anglFirst)
        customListener?.onVariableChanged(angleF)
        customListener?.onVariableChanged(angleF2)

        DataHolder.angle = anglFirst
        DataHolder.angle2 = angleF
        DataHolder.angle3 = angleF2


        if(tentacle[2]!!.angle <= tentacle[1]!!.angle) {
            tentacle[2]!!.angle = tentacle[1]!!.angle
            tentacle[1]!!.follow(endX, endY)
        }

        if(tentacle[1]!!.angle <= tentacle[0]!!.angle) {
            tentacle[1]!!.angle = tentacle[0]!!.angle
        }

        for(i in 1 until lenght)
        {
            tentacle[i]!!.positionA(tentacle[i-1]!!.b)
        }

        for(i in 0 until lenght)
        {
            tentacle[i]!!.draw(canvas, paint)
        }


        //experiment

        canvas.drawCircle(xM, yM, radius.toFloat(), mainCircle)

        canvas.drawCircle(endX, endY, 5f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {

            MotionEvent.ACTION_MOVE -> {
                invalidate() // Перерисовываем экран

                /* for (i in 1..2000) {
                     println(i)
                 }*/
                endX = x
                endY = y
            }
        }


        return true
    }

    fun setCustomDrawableViewListener(listener: CustomDrawableViewListener) {
        customListener = listener
    }
}