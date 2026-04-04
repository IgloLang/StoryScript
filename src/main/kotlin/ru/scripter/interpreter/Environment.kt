package ru.scripter.interpreter

// ============== Environment (Scope) ==============
// Окружение хранит переменные и функции, поддерживает вложенные области видимости
class Environment(private val parent: Environment? = null) {
    private val values = mutableMapOf<String, ru.scripter.interpreter.Value>()
    
    fun define(name: String, value: ru.scripter.interpreter.Value) {
        values[name] = value
    }
    
    fun get(name: String): ru.scripter.interpreter.Value {
        return values[name] ?: parent?.get(name) ?: throw RuntimeException("Undefined variable: $name")
    }
    
    fun set(name: String, value: ru.scripter.interpreter.Value) {
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
