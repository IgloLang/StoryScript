package ru.scripter.init

import java.io.File
import ru.scripter.cs.*
import ru.scripter.lexer.Lexer
import ru.scripter.parser.Parser
import ru.scripter.interpreter.Interpreter
import ru.scripter.interpreter.NumberValue
import ru.scripter.interpreter.StringValue
import ru.scripter.interpreter.BooleanValue
import ru.scripter.interpreter.ObjectValue
import ru.scripter.interpreter.NullValue

class MainInit(val scriptPath: String) {
    fun run() {
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
            
            setupGlobalObjects(interpreter)
            setupGlobalFunctions(interpreter)
            
            interpreter.interpret(program)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun setupGlobalObjects(interpreter: Interpreter) {
        interpreter.addGlobalObject("world", WorldAPI())
        interpreter.addGlobalObject("input", ScannerAPI())
        interpreter.addGlobalObject("math", MathAPI())
        interpreter.addGlobalObject("string", StringAPI())
        interpreter.addGlobalObject("random", RandomAPI())
        interpreter.addGlobalObject("time", TimeAPI())
        interpreter.addGlobalObject("file", FileAPI())
        interpreter.addGlobalObject("console", ConsoleAPI())
    }
    
    private fun setupGlobalFunctions(interpreter: Interpreter) {
        val scannerAPI = ScannerAPI()
        
        interpreter.addGlobalFunction("entity") { args ->
            if (args.isNotEmpty() && args[0] is NumberValue) {
                val entityValue = (args[0] as NumberValue).value.toInt()
                ObjectValue(EntityAPI(entityValue))
            } else {
                NullValue
            }
        }
        
        interpreter.addGlobalFunction("input") { args ->
            val prompt = if (args.isNotEmpty() && args[0] is StringValue) {
                (args[0] as StringValue).value
            } else {
                ""
            }
            StringValue(scannerAPI.readLine(prompt))
        }
        
        interpreter.addGlobalFunction("int") { args ->
            if (args.isNotEmpty()) {
                val value = when (args[0]) {
                    is StringValue -> {
                        try {
                            (args[0] as StringValue).value.toInt()
                        } catch (e: Exception) {
                            0
                        }
                    }
                    is NumberValue -> {
                        (args[0] as NumberValue).value.toInt()
                    }
                    is BooleanValue -> {
                        if ((args[0] as BooleanValue).value) 1 else 0
                    }
                    else -> 0
                }
                NumberValue(value.toDouble())
            } else {
                NumberValue(0.0)
            }
        }
        
        interpreter.addGlobalFunction("float") { args ->
            if (args.isNotEmpty()) {
                val value = when (args[0]) {
                    is StringValue -> {
                        try {
                            (args[0] as StringValue).value.toDouble()
                        } catch (e: Exception) {
                            0.0
                        }
                    }
                    is NumberValue -> {
                        (args[0] as NumberValue).value
                    }
                    else -> 0.0
                }
                NumberValue(value)
            } else {
                NumberValue(0.0)
            }
        }
        
        interpreter.addGlobalFunction("double") { args ->
            if (args.isNotEmpty()) {
                val value = when (args[0]) {
                    is StringValue -> {
                        try {
                            (args[0] as StringValue).value.toDouble()
                        } catch (e: Exception) {
                            0.0
                        }
                    }
                    is NumberValue -> {
                        (args[0] as NumberValue).value
                    }
                    else -> 0.0
                }
                NumberValue(value)
            } else {
                NumberValue(0.0)
            }
        }
        
        interpreter.addGlobalFunction("str") { args ->
            if (args.isNotEmpty()) {
                StringValue(args[0].toStringValue())
            } else {
                StringValue("null")
            }
        }
        
        interpreter.addGlobalFunction("typeof") { args ->
            if (args.isNotEmpty()) {
                val typeName = when (args[0]) {
                    is NumberValue -> {
                        val num = (args[0] as NumberValue).value
                        when {
                            num == num.toLong().toDouble() -> "Int"
                            else -> "Float"
                        }
                    }
                    is StringValue -> "String"
                    is BooleanValue -> "Boolean"
                    is ObjectValue -> "Object"
                    else -> "Unknown"
                }
                StringValue(typeName)
            } else {
                StringValue("Unknown")
            }
        }
        
        interpreter.addGlobalFunction("format") { args ->
            if (args.isNotEmpty()) {
                val value = args[0]
                val valueStr = value.toStringValue()
                val type = when (value) {
                    is NumberValue -> {
                        val num = value.value
                        when {
                            num == num.toLong().toDouble() -> "Int"
                            else -> "Float"
                        }
                    }
                    is StringValue -> "String"
                    else -> "Unknown"
                }
                StringValue("$valueStr [$type]")
            } else {
                StringValue("null [Null]")
            }
        }
    }
}