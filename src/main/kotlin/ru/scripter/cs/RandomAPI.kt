package ru.scripter.cs

import kotlin.random.Random

class RandomAPI {
    private val random = Random
    
    // Случайное число в диапазоне [0, 1)
    fun random(): Double = random.nextDouble()
    
    // Случайное целое число до max (не включая max)
    fun nextInt(max: Int): Int = if (max > 0) random.nextInt(max) else 0
    
    // Случайное целое число в диапазоне [min, max)
    fun nextInt(min: Int, max: Int): Int {
        return if (max > min) random.nextInt(min, max) else min
    }
    
    // Случайное число с плавающей точкой до max
    fun nextDouble(max: Double): Double = random.nextDouble(max)
    
    // Случайное число с плавающей точкой в диапазоне [min, max)
    fun nextDouble(min: Double, max: Double): Double {
        return if (max > min) random.nextDouble(min, max) else min
    }
    
    // Случайный Boolean
    fun nextBoolean(): Boolean = random.nextBoolean()
    
    // Случайный элемент из списка
    fun choice(items: List<Any>): Any = if (items.isNotEmpty()) items[random.nextInt(items.size)] else ""
    
    // Перемешивание списка
    fun shuffle(items: List<Any>): List<Any> = items.shuffled()
    
    // Случайное число от 1 до 100
    fun random100(): Int = random.nextInt(1, 101)
    
    // Случайное число от 1 до 6 (как кубик)
    fun dice(): Int = random.nextInt(1, 7)
}
