package ru.danila.math.polynomial

infix fun Double.eq(other: Double) = Math.abs(this-other) < Math.max(Math.ulp(this),Math.ulp(other))*10
infix fun Double.neq(other: Double) = Math.abs(this-other) >= Math.max(Math.ulp(this),Math.ulp(other))*10
infix fun Double.les(other: Double) = this < other && this neq other