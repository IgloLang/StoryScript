package org.example.interpreter

fun interface ActiveRunnable {
    fun run()
}

// Internal class to wrap script blocks as ActiveRunnable
class ScriptActiveRunnable(
    private val block: FunctionValue,
    private val interpreter: Interpreter,
    private val closure: Environment
) : ActiveRunnable {
    override fun run() {
        val previousEnv = interpreter.currentEnv
        interpreter.currentEnv = Environment(closure)
        
        try {
            interpreter.executeBlock(block.body)
        } catch (e: ReturnException) {
            // Catch return from block
        } finally {
            interpreter.currentEnv = previousEnv
        }
    }
}