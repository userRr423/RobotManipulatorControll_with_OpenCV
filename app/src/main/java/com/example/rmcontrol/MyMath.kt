package com.example.rmcontrol

import android.content.res.Resources
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

class MyMath {

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun convert(degree: Float): Float {
        val pi = PI.toFloat()
        return degree * (pi / 180)
    }

    fun heading(vec: Vec2): Float {
        val heading = atan2(vec.y, vec.x) * 180 / PI.toFloat()
        return heading
    }

    fun calculateSlope(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return (y2 - y1) / (x2 - x1)
    }

    fun calculateAngleBetweenLines(m1: Float, m2: Float): Float {
        val angle = Math.atan((m2 - m1) / (1 + m1 * m2).toDouble()) * 180 / PI.toFloat()
        return angle.toFloat()
    }

    fun distanceLine(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(Math.pow((x2 - x1).toDouble(), 2.0) + Math.pow((y2 - y1).toDouble(), 2.0)).toFloat()
    }

    fun calculateAngleBetweenPoints(A: Vec2, B: Vec2): Float {
        var angle = atan2(A.y - B.y, A.x - B.x)
        angle = angle * 180 / PI.toFloat()
        return angle
    }

    fun mapRange(value: Float, fromLow: Float, fromHigh: Float, toLow: Float, toHigh: Float): Float {
        return toLow + (value - fromLow) * (toHigh - toLow) / (fromHigh - fromLow)
    }

    fun distancePoint(v1: Vec2, v2: Vec2): Float {
        return Math.sqrt((v2.x - v1.x).toDouble().pow(2) + (v2.y - v1.y).toDouble().pow(2)).toFloat()
    }

    fun angleBetweenVectors(vector1: Vec2, vector2: Vec2): Float {
        val dotProduct = vector1.x * vector2.x + vector1.y * vector2.y
        val magnitudesProduct = Math.sqrt((vector1.x * vector1.x + vector1.y * vector1.y).toDouble()) * Math.sqrt(
            (vector2.x * vector2.x + vector2.y * vector2.y).toDouble()
        )

        if (magnitudesProduct.toFloat() == 0f) {
            return 0.0f // Возвращаем 0 градусов, если хотя бы один из векторов имеет нулевую длину
        }

        val angleRad = Math.acos(dotProduct / magnitudesProduct)
        val angleDeg = angleRad * (180.0f / PI.toFloat())

        return angleDeg.toFloat()
    }

    fun setMag(vector: Vec2, newMag: Float): Vec2 {
        val currentMag = Math.sqrt((vector.x * vector.x + vector.y * vector.y).toDouble())

        if (currentMag.toFloat() != 0f) {
            val factor = newMag / currentMag
            return Vec2((vector.x * factor).toFloat(), (vector.y * factor).toFloat())
        } else {
            return Vec2(0f, 0f)
        }
    }

    fun isPointInsideCircle(circleX: Float, circleY: Float, radius: Float, pointX: Float, pointY: Float): Boolean {
        val distance = sqrt(Math.pow(pointX - circleX.toDouble(), 2.0) + Math.pow(pointY - circleY.toDouble(), 2.0))
        return distance <= radius
    }

    fun calculateAngleBetweenVectors(vector1: Vec2, vector2: Vec2): Float {
        // Calculate the dot product of the two vectors
        val dotProduct = vector1.x * vector2.x + vector1.y * vector2.y

        // Calculate the magnitudes of the two vectors
        val magnitude1 = sqrt(vector1.x * vector1.x + vector1.y * vector1.y)
        val magnitude2 = sqrt(vector2.x * vector2.x + vector2.y * vector2.y)

        // Calculate the cosine of the angle between the two vectors
        val cosine = dotProduct / (magnitude1 * magnitude2)

        // Calculate the angle in radians
        val angleInRadians = acos(cosine)

        // Convert the angle to degrees
        val angleInDegrees = angleInRadians * 180.0 / Math.PI

        return angleInDegrees.toFloat()
    }


}