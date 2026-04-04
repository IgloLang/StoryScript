package ru.scripter.cs

class ListAPI {
    private val items = mutableListOf<Any?>()
    
    fun add(item: Any?) {
        items.add(item)
    }
    
    fun remove(index: Int): Any? {
        return if (index in 0 until items.size) {
            items.removeAt(index)
        } else {
            null
        }
    }
    
    fun remove(item: Any?): Boolean {
        return items.remove(item)
    }
    
    fun get(index: Int): Any? {
        return if (index in 0 until items.size) {
            items[index]
        } else {
            null
        }
    }
    
    fun size(): Int {
        return items.size
    }
    
    fun clear() {
        items.clear()
    }
    
    fun contains(item: Any?): Boolean {
        return items.contains(item)
    }
    
    fun indexOf(item: Any?): Int {
        return items.indexOf(item)
    }
    
    override fun toString(): String {
        return items.joinToString(", ", "[", "]") { 
            when (it) {
                null -> "null"
                is String -> it
                else -> it.toString()
            }
        }
    }
}
