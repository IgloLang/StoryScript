package ru.scripter.cs

import ru.scripter.interpreter.ActiveRunnable
import java.util.concurrent.TimeUnit

class WorldAPI {
    fun send(text: String) {
        println(text)
    }
    fun send(name: String, text: String) {
        println("[$name] $text")
    }
    fun time(timer: Int, runnable: ru.scripter.interpreter.ActiveRunnable) {
        TimeUnit.SECONDS.sleep(timer.toLong())
        runnable.run()
    }
}