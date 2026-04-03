package org.example

import java.io.File
import org.example.lexer.Lexer
import org.example.parser.Parser
import org.example.interpreter.Interpreter
import org.example.cs.WorldAPI
import org.example.cs.EntityAPI
import org.example.cs.ScannerAPI

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
        
        val scannerAPI = ScannerAPI()
        interpreter.addGlobalObject("input", scannerAPI)

        interpreter.addGlobalFunction("entity") { args ->
            if (args.isNotEmpty() && args[0] is org.example.interpreter.NumberValue) {
                val entityValue = (args[0] as org.example.interpreter.NumberValue).value.toInt()
                org.example.interpreter.ObjectValue(EntityAPI(entityValue))
            } else {
                org.example.interpreter.NullValue
            }
        }
        
        // Функция для чтения строки с промптом
        interpreter.addGlobalFunction("input") { args ->
            val prompt = if (args.isNotEmpty() && args[0] is org.example.interpreter.StringValue) {
                (args[0] as org.example.interpreter.StringValue).value
            } else {
                ""
            }
            org.example.interpreter.StringValue(scannerAPI.readLine(prompt))
        }
        
        // Функция для конвертации в Int
        interpreter.addGlobalFunction("int") { args ->
            if (args.isNotEmpty()) {
                val value = when (args[0]) {
                    is org.example.interpreter.StringValue -> {
                        try {
                            (args[0] as org.example.interpreter.StringValue).value.toInt()
                        } catch (e: Exception) {
                            0
                        }
                    }
                    is org.example.interpreter.NumberValue -> {
                        (args[0] as org.example.interpreter.NumberValue).value.toInt()
                    }
                    is org.example.interpreter.BooleanValue -> {
                        if ((args[0] as org.example.interpreter.BooleanValue).value) 1 else 0
                    }
                    else -> 0
                }
                org.example.interpreter.NumberValue(value.toDouble())
            } else {
                org.example.interpreter.NumberValue(0.0)
            }
        }
        
        // Функция для конвертации в Float/Double
        interpreter.addGlobalFunction("float") { args ->
            if (args.isNotEmpty()) {
                val value = when (args[0]) {
                    is org.example.interpreter.StringValue -> {
                        try {
                            (args[0] as org.example.interpreter.StringValue).value.toDouble()
                        } catch (e: Exception) {
                            0.0
                        }
                    }
                    is org.example.interpreter.NumberValue -> {
                        (args[0] as org.example.interpreter.NumberValue).value
                    }
                    else -> 0.0
                }
                org.example.interpreter.NumberValue(value)
            } else {
                org.example.interpreter.NumberValue(0.0)
            }
        }
        
        // Другой вариант для Double (синоним float)
        interpreter.addGlobalFunction("double") { args ->
            if (args.isNotEmpty()) {
                val value = when (args[0]) {
                    is org.example.interpreter.StringValue -> {
                        try {
                            (args[0] as org.example.interpreter.StringValue).value.toDouble()
                        } catch (e: Exception) {
                            0.0
                        }
                    }
                    is org.example.interpreter.NumberValue -> {
                        (args[0] as org.example.interpreter.NumberValue).value
                    }
                    else -> 0.0
                }
                org.example.interpreter.NumberValue(value)
            } else {
                org.example.interpreter.NumberValue(0.0)
            }
        }
        
        // Функция для конвертации в String
        interpreter.addGlobalFunction("str") { args ->
            if (args.isNotEmpty()) {
                org.example.interpreter.StringValue(args[0].toStringValue())
            } else {
                org.example.interpreter.StringValue("null")
            }
        }
        
        // Функция для получения типа значения
        interpreter.addGlobalFunction("typeof") { args ->
            if (args.isNotEmpty()) {
                val typeName = when (args[0]) {
                    is org.example.interpreter.NumberValue -> {
                        val num = (args[0] as org.example.interpreter.NumberValue).value
                        when {
                            num == num.toLong().toDouble() -> "Int"
                            else -> "Float"
                        }
                    }
                    is org.example.interpreter.StringValue -> "String"
                    is org.example.interpreter.BooleanValue -> "Boolean"
                    is org.example.interpreter.ObjectValue -> "Object"
                    is org.example.interpreter.FunctionValue -> "Function"
                    is org.example.interpreter.NullValue -> "Null"
                    else -> "Unknown"
                }
                org.example.interpreter.StringValue(typeName)
            } else {
                org.example.interpreter.StringValue("Unknown")
            }
        }
        
        // Функция для форматирования числа с указанием типа
        interpreter.addGlobalFunction("format") { args ->
            if (args.isNotEmpty()) {
                val value = args[0]
                val valueStr = value.toStringValue()
                val type = when (value) {
                    is org.example.interpreter.NumberValue -> {
                        val num = value.value
                        when {
                            num == num.toLong().toDouble() -> "Int"
                            else -> "Float"
                        }
                    }
                    is org.example.interpreter.StringValue -> "String"
                    else -> "Unknown"
                }
                org.example.interpreter.StringValue("$valueStr [$type]")
            } else {
                org.example.interpreter.StringValue("null [Null]")
            }
        }
        
        interpreter.interpret(program)
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}