package org.example.interpreter

// ============== Environment (Scope) ==============
// Окружение хранит переменные и функции, поддерживает вложенные области видимости
class Environment(private val parent: Environment? = null) {
    private val values = mutableMapOf<String, Value>()
    
    fun define(name: String, value: Value) {
        values[name] = value
    }
    
    fun get(name: String): Value {
        return values[name] ?: parent?.get(name) ?: throw RuntimeException("Undefined variable: $name")
    }
    
    fun set(name: String, value: Value) {
        if (values.containsKey(name)) {
            values[name] = value
        } else if (parent != null) {
            parent.set(name, value)
        } else {
            throw RuntimeException("Undefined variable: $name")
        }
    }
    
    fun exists(name: String): Boolean {
        return values.containsKey(name) || (parent != null && parent.exists(name))
    }
}
