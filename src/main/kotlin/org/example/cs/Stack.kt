package org.example.cs

class Stack {
    private val data = mutableListOf<Any?>()
    
    fun push(item: Any?) {
        data.add(item)
    }
    
    fun pop(): Any? {
        return if (data.isNotEmpty()) data.removeAt(data.size - 1) else null
    }
    
    fun peek(): Any? {
        return if (data.isNotEmpty()) data[data.size - 1] else null
    }
    
    fun size(): Int {
        return data.size
    }
    
    fun isEmpty(): Boolean {
        return data.isEmpty()
    }
    
    fun clear() {
        data.clear()
    }
    
    override fun toString(): String {
        return data.reversed().joinToString(", ", "[", "]")
    }
}
