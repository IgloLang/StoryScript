package ru.scripter.cs

class Counter {
    private var count = 0
    
    fun increment(): Int {
        count++
        return count
    }
    
    fun decrement(): Int {
        count--
        return count
    }
    
    fun add(value: Int): Int {
        count += value
        return count
    }
    
    fun subtract(value: Int): Int {
        count -= value
        return count
    }
    
    fun reset() {
        count = 0
    }
    
    fun get(): Int {
        return count
    }
    
    fun set(value: Int) {
        count = value
    }
    
    override fun toString(): String {
        return count.toString()
    }
}
