package ru.scripter.interpreter

fun interface ActiveRunnable {
    fun run()
}

// Internal class to wrap script blocks as ActiveRunnable
class ScriptActiveRunnable(
    private val block: ru.scripter.interpreter.FunctionValue,
    private val interpreter: ru.scripter.interpreter.Interpreter,
    private val closure: ru.scripter.interpreter.Environment
) : ru.scripter.interpreter.ActiveRunnable {
    override fun run() {
        val previousEnv = interpreter.currentEnv
        interpreter.currentEnv = _root_ide_package_.ru.scripter.interpreter.Environment(closure)
        
        try {
            interpreter.executeBlock(block.body)
        } catch (e: ru.scripter.interpreter.ReturnException) {
            // Catch return from block
        } finally {
            interpreter.currentEnv = previousEnv
        }
    }
}