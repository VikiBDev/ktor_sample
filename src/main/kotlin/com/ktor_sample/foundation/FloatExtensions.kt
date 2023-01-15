package com.ktor_sample.foundation

import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.roundToDigits(digits: Int): Float {
    val exponent = 10f.pow(digits)
    return (this * exponent).roundToInt() / exponent
}