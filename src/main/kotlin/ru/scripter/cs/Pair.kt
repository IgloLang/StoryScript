package ru.scripter.cs

class Pair(var first: Any? = null, var second: Any? = null) {
    
    fun swap() {
        val temp = first
        first = second
        second = temp
    }
    
    override fun toString(): String {
        return "($first, $second)"
    }
}
