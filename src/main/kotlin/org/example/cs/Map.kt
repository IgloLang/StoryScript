package org.example.cs

class Map {
    private val data = mutableMapOf<String, Any?>()
    
    fun set(key: String, value: Any?) {
        data[key] = value
    }
    
    fun get(key: String): Any? {
        return data[key]
    }
    
    fun remove(key: String): Any? {
        return data.remove(key)
    }
    
    fun has(key: String): Boolean {
        return data.containsKey(key)
    }
    
    fun size(): Int {
        return data.size
    }
    
    fun clear() {
        data.clear()
    }
    
    fun keys(): List<String> {
        return data.keys.toList()
    }
    
    override fun toString(): String {
        return data.entries.joinToString(", ", "{", "}") { (k, v) ->
            "$k: $v"
        }
    }
}
