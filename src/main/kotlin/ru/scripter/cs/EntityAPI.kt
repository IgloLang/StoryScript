package ru.scripter.cs

class EntityAPI(test: Int) {
    var name_ai: String = "entity"
    var test: Int = 0

    init {
        this.test = test
    }

    fun send_return(): Int {
        return test
    }

    fun send() {
        println("[$name_ai] $test")
    }
}