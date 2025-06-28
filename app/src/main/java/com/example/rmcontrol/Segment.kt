package com.example.rmcontrol

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.cos
import kotlin.math.sin

class Segment() {
    var a = Vec2(0f, 0f)
    var b = Vec2(0f, 0f)
    var len: Float = 0.0f
    var angle: Float = 0.0f
    var selfAngle: Float = 0.0f
    var parent: Segment? = null
    var degreesAngle: Float = 0f

    var child: Segment? = null
    var selfAngle1: Float = 0f
    var move: Boolean = true
    var myMath = MyMath()


    constructor(parent: Segment, len_: Float, angle_: Float) : this() {
        a = parent.b
        len = len_
        angle = angle_
        calculateB()
    }

    constructor(x: Float, y: Float, len_: Float, angle_: Float) : this() {
        a = Vec2(x, y)
        len = len_
        angle = angle_
        calculateB()
    }


    fun calculateB() {
        var dx:Float = len * cos(angle.toDouble()).toFloat()
        var dy:Float = len * sin(angle.toDouble()).toFloat()
        b = Vec2(a.x+dx, a.y+dy)
    }

    fun  update() {
        calculateB()
    }

    fun follow(tx: Float, ty: Float) {
        var target = Vec2(tx, ty)
        var dir = target - a

        if (move) {
            angle = myMath.convert(myMath.heading(dir)).toFloat()
        }

        degreesAngle = myMath.heading(dir)

        dir = myMath.setMag(dir, len)
        var mult = -1
        dir = dir.times(mult.toFloat())

        a = target + dir
    }


    fun follow(child: Segment) {
        var targetX:Float = child.a.x
        var targetY:Float = child.a.y

        follow(targetX, targetY)
    }

    fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawLine(a.x, a.y, b.x, b.y, paint)
    }

    fun positionA(base:Vec2) {
        a = base
        calculateB()
    }

    fun update(t: Boolean) {
        angle = angle.coerceIn(myMath.convert(-170f), 0.0f)
        calculateB()
    }
}