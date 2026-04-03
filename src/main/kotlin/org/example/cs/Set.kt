package org.example.cs

class Set {
    private val data = mutableSetOf<Any?>()
    
    fun add(item: Any?) {
        data.add(item)
    }
    
    fun remove(item: Any?): Boolean {
        return data.remove(item)
    }
    
    fun has(item: Any?): Boolean {
        return data.contains(item)
    }
    
    fun size(): Int {
        return data.size
    }
    
    fun clear() {
        data.clear()
    }
    
    fun toList(): List<Any?> {
        return data.toList()
    }
    
    override fun toString(): String {
        return data.joinToString(", ", "{", "}")
    }
}
