package com.example.rmcontrol

import kotlin.math.sqrt

data class Vec2(var x: Float, var y: Float) {

    operator fun plus(other: Vec2): Vec2 {
        return Vec2(this.x + other.x, this.y + other.y)
    }

    operator fun minus(other: Vec2): Vec2 {
        return Vec2(this.x - other.x, this.y - other.y)
    }

    operator fun times(scalar: Float): Vec2 {
        return Vec2(this.x * scalar, this.y * scalar)
    }

    operator fun times(other: Vec2): Float {
        return this.x * other.x + this.y * other.y
    }

    fun normalize(): Vec2 {
        val len = length()
        return if(len > 0) Vec2(x / len, y / len) else this
    }

    operator fun div(scalar: Float): Vec2 {
        return Vec2(this.x / scalar, this.y / scalar)
    }

    fun length(): Float {
        return sqrt(this.x*this.x + this.y*this.y)
    }

    override fun toString(): String {
        return "Vec2(x=$x, y=$y)"
    }
}