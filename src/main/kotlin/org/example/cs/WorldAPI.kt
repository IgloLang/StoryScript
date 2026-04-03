package org.example.cs

import org.example.interpreter.ActiveRunnable
import java.util.concurrent.TimeUnit

class WorldAPI {
    fun send(text: String) {
        println(text)
    }
    fun send(name: String, text: String) {
        println("[$name] $text")
    }
    fun time(timer: Int, runnable: ActiveRunnable) {
        TimeUnit.SECONDS.sleep(timer.toLong())
        runnable.run()
    }
}