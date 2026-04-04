package ru.scripter.cs

class StringBuilder {
    private val buffer = mutableListOf<String>()
    
    fun append(text: Any?): String {
        buffer.add(text?.toString() ?: "null")
        return toString()
    }
    
    fun append(char: String): String {
        if (char.isNotEmpty()) {
            buffer.add(char[0].toString())
        }
        return toString()
    }
    
    fun insert(index: Int, text: Any?) {
        val str = text?.toString() ?: "null"
        if (index in 0..buffer.size) {
            buffer.add(index, str)
        }
    }
    
    fun delete(start: Int, end: Int) {
        var current = start
        while (current < end && current < buffer.size) {
            if (current >= 0) {
                buffer.removeAt(current)
            }
            current++
        }
    }
    
    fun reverse(): String {
        buffer.reverse()
        return toString()
    }
    
    fun clear() {
        buffer.clear()
    }
    
    fun length(): Int {
        return buffer.sumOf { it.length }
    }
    
    fun toUpperCase(): String {
        return toString().uppercase()
    }
    
    fun toLowerCase(): String {
        return toString().lowercase()
    }
    
    override fun toString(): String {
        return buffer.joinToString("")
    }
}
