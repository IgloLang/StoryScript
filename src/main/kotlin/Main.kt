package org.example

import java.io.File
import org.example.lexer.Lexer
import org.example.parser.Parser
import org.example.interpreter.Interpreter
import org.example.cs.WorldAPI
import org.example.cs.EntityAPI

fun main(args: Array<String>) {
    val scriptPath = if (args.isNotEmpty()) {
        args[0]
    } else {
        "script.st"  // Default script file
    }
    
    val scriptFile = File(scriptPath)
    
    if (!scriptFile.exists()) {
        println("Error: File not found: $scriptPath")
        return
    }
    
    try {
        val source = scriptFile.readText()
        val lexer = Lexer(source)
        val tokens = lexer.tokenize()
        val parser = Parser(tokens)
        val program = parser.parse()
        val interpreter = Interpreter()
        interpreter.addGlobalObject("world", WorldAPI())
        
        interpreter.addGlobalFunction("entity") { args ->
            if (args.isNotEmpty() && args[0] is org.example.interpreter.NumberValue) {
                val entityValue = (args[0] as org.example.interpreter.NumberValue).value.toInt()
                org.example.interpreter.ObjectValue(EntityAPI(entityValue))
            } else {
                org.example.interpreter.NullValue
            }
        }
        
        interpreter.interpret(program)
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}