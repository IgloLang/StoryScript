package org.example.cs

class Queue {
    private val data = mutableListOf<Any?>()
    
    fun enqueue(item: Any?) {
        data.add(item)
    }
    
    fun dequeue(): Any? {
        return if (data.isNotEmpty()) data.removeAt(0) else null
    }
    
    fun front(): Any? {
        return if (data.isNotEmpty()) data[0] else null
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
        return data.joinToString(", ", "[", "]")
    }
}
