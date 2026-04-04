package ru.scripter.interpreter

import ru.scripter.parser.BlockStatement

// ============== Value Representation ==============
// Базовые значения в интерпретаторе
sealed class Value {
    abstract fun toBoolean(): Boolean
    abstract fun toNumber(): Double
    abstract fun toStringValue(): String
}

data class NumberValue(val value: Double) : Value() {
    override fun toBoolean(): Boolean = value != 0.0
    override fun toNumber(): Double = value
    override fun toStringValue(): String = if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()
}

data class StringValue(val value: String) : Value() {
    override fun toBoolean(): Boolean = value.isNotEmpty()
    override fun toNumber(): Double = value.toDoubleOrNull() ?: 0.0
    override fun toStringValue(): String = value
}

data class BooleanValue(val value: Boolean) : Value() {
    override fun toBoolean(): Boolean = value
    override fun toNumber(): Double = if (value) 1.0 else 0.0
    override fun toStringValue(): String = value.toString()
}

object NullValue : Value() {
    override fun toBoolean(): Boolean = false
    override fun toNumber(): Double = 0.0
    override fun toStringValue(): String = "null"
}

data class FunctionValue(val params: List<String>, val body: BlockStatement, val closure: ru.scripter.interpreter.Environment) : Value() {
    override fun toBoolean(): Boolean = true
    override fun toNumber(): Double = 0.0
    override fun toStringValue(): String = "[Function]"
}

data class ObjectValue(val obj: Any) : Value() {
    override fun toBoolean(): Boolean = true
    override fun toNumber(): Double = 0.0
    override fun toStringValue(): String = obj.toString()
}

data class BuiltinFunction(val function: (List<Value>) -> Value) : Value() {
    override fun toBoolean(): Boolean = true
    override fun toNumber(): Double = 0.0
    override fun toStringValue(): String = "[BuiltinFunction]"
}
