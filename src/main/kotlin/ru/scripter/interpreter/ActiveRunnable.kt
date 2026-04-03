package org.example.interpreter

fun interface ActiveRunnable {
    fun run()
}

// Internal class to wrap script blocks as ActiveRunnable
class ScriptActiveRunnable(
    private val block: org.example.interpreter.FunctionValue,
    private val interpreter: org.example.interpreter.Interpreter,
    private val closure: org.example.interpreter.Environment
) : org.example.interpreter.ActiveRunnable {
    override fun run() {
        val previousEnv = interpreter.currentEnv
        interpreter.currentEnv = _root_ide_package_.org.example.interpreter.Environment(closure)
        
        try {
            interpreter.executeBlock(block.body)
        } catch (e: org.example.interpreter.ReturnException) {
            // Catch return from block
        } finally {
            interpreter.currentEnv = previousEnv
        }
    }
}