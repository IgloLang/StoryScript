package org.example.cs

import kotlin.math.*

class MathAPI {
    // Базовые математические функции
    fun abs(value: Double): Double = kotlin.math.abs(value)
    fun abs(value: Int): Double = kotlin.math.abs(value).toDouble()
    
    fun sqrt(value: Double): Double {
        if (value < 0) throw RuntimeException("sqrt of negative number")
        return kotlin.math.sqrt(value)
    }
    fun sqrt(value: Int): Double {
        if (value < 0) throw RuntimeException("sqrt of negative number")
        return kotlin.math.sqrt(value.toDouble())
    }
    
    fun pow(base: Double, exponent: Double): Double = base.pow(exponent)
    fun pow(base: Int, exponent: Int): Double = base.toDouble().pow(exponent.toDouble())
    fun pow(base: Double, exponent: Int): Double = base.pow(exponent.toDouble())
    fun pow(base: Int, exponent: Double): Double = base.toDouble().pow(exponent)
    
    fun min(a: Double, b: Double): Double = kotlin.math.min(a, b)
    fun min(a: Int, b: Int): Int = kotlin.math.min(a, b)
    
    fun max(a: Double, b: Double): Double = kotlin.math.max(a, b)
    fun max(a: Int, b: Int): Int = kotlin.math.max(a, b)
    
    // Тригонометрические функции
    fun sin(angle: Double): Double = kotlin.math.sin(angle)
    fun sin(angle: Int): Double = kotlin.math.sin(angle.toDouble())
    
    fun cos(angle: Double): Double = kotlin.math.cos(angle)
    fun cos(angle: Int): Double = kotlin.math.cos(angle.toDouble())
    
    fun tan(angle: Double): Double = kotlin.math.tan(angle)
    fun tan(angle: Int): Double = kotlin.math.tan(angle.toDouble())
    
    // Логарифмические функции
    fun log(value: Double): Double {
        if (value <= 0) throw RuntimeException("log of non-positive number")
        return kotlin.math.ln(value)
    }
    fun log(value: Int): Double {
        if (value <= 0) throw RuntimeException("log of non-positive number")
        return kotlin.math.ln(value.toDouble())
    }
    
    fun log10(value: Double): Double {
        if (value <= 0) throw RuntimeException("log10 of non-positive number")
        return kotlin.math.log10(value)
    }
    fun log10(value: Int): Double {
        if (value <= 0) throw RuntimeException("log10 of non-positive number")
        return kotlin.math.log10(value.toDouble())
    }
    
    fun exp(value: Double): Double = kotlin.math.exp(value)
    fun exp(value: Int): Double = kotlin.math.exp(value.toDouble())
    
    // Округление
    fun round(value: Double): Double = kotlin.math.round(value)
    fun round(value: Int): Int = value
    
    fun floor(value: Double): Double = kotlin.math.floor(value)
    fun floor(value: Int): Int = value
    
    fun ceil(value: Double): Double = kotlin.math.ceil(value)
    fun ceil(value: Int): Int = value
    
    // Константы
    fun pi(): Double = Math.PI
    
    fun e(): Double = Math.E
}
