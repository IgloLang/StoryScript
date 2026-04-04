package ru.scripter

import java.io.File
import ru.scripter.cs.*
import ru.scripter.lexer.Lexer

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
        val parser = _root_ide_package_.ru.scripter.parser.Parser(tokens)
        val program = parser.parse()
        val interpreter = _root_ide_package_.ru.scripter.interpreter.Interpreter()
        interpreter.addGlobalObject("world", _root_ide_package_.ru.scripter.cs.WorldAPI())
        
        val scannerAPI = _root_ide_package_.ru.scripter.cs.ScannerAPI()
        interpreter.addGlobalObject("input", scannerAPI)
        
        // Регистрация API объектов
        interpreter.addGlobalObject("math", _root_ide_package_.ru.scripter.cs.MathAPI())
        interpreter.addGlobalObject("string", _root_ide_package_.ru.scripter.cs.StringAPI())
        interpreter.addGlobalObject("random", _root_ide_package_.ru.scripter.cs.RandomAPI())
        interpreter.addGlobalObject("time", _root_ide_package_.ru.scripter.cs.TimeAPI())
        interpreter.addGlobalObject("file", _root_ide_package_.ru.scripter.cs.FileAPI())
        interpreter.addGlobalObject("console", _root_ide_package_.ru.scripter.cs.ConsoleAPI())

        interpreter.addGlobalFunction("entity") { args ->
            if (args.isNotEmpty() && args[0] is ru.scripter.interpreter.NumberValue) {
                val entityValue = (args[0] as ru.scripter.interpreter.NumberValue).value.toInt()
                _root_ide_package_.ru.scripter.interpreter.ObjectValue(
                    _root_ide_package_.ru.scripter.cs.EntityAPI(
                        entityValue
                    )
                )
            } else {
                _root_ide_package_.ru.scripter.interpreter.NullValue
            }
        }
        
        // Функция для чтения строки с промптом
        interpreter.addGlobalFunction("input") { args ->
            val prompt = if (args.isNotEmpty() && args[0] is ru.scripter.interpreter.StringValue) {
                (args[0] as ru.scripter.interpreter.StringValue).value
            } else {
                ""
            }
            _root_ide_package_.ru.scripter.interpreter.StringValue(scannerAPI.readLine(prompt))
        }
        
        // Функция для конвертации в Int
        interpreter.addGlobalFunction("int") { args ->
            if (args.isNotEmpty()) {
                val value = when (args[0]) {
                    is ru.scripter.interpreter.StringValue -> {
                        try {
                            (args[0] as ru.scripter.interpreter.StringValue).value.toInt()
                        } catch (e: Exception) {
                            0
                        }
                    }
                    is ru.scripter.interpreter.NumberValue -> {
                        (args[0] as ru.scripter.interpreter.NumberValue).value.toInt()
                    }
                    is ru.scripter.interpreter.BooleanValue -> {
                        if ((args[0] as ru.scripter.interpreter.BooleanValue).value) 1 else 0
                    }
                    else -> 0
                }
                _root_ide_package_.ru.scripter.interpreter.NumberValue(value.toDouble())
            } else {
                _root_ide_package_.ru.scripter.interpreter.NumberValue(0.0)
            }
        }
        
        // Функция для конвертации в Float/Double
        interpreter.addGlobalFunction("float") { args ->
            if (args.isNotEmpty()) {
                val value = when (args[0]) {
                    is ru.scripter.interpreter.StringValue -> {
                        try {
                            (args[0] as ru.scripter.interpreter.StringValue).value.toDouble()
                        } catch (e: Exception) {
                            0.0
                        }
                    }
                    is ru.scripter.interpreter.NumberValue -> {
                        (args[0] as ru.scripter.interpreter.NumberValue).value
                    }
                    else -> 0.0
                }
                _root_ide_package_.ru.scripter.interpreter.NumberValue(value)
            } else {
                _root_ide_package_.ru.scripter.interpreter.NumberValue(0.0)
            }
        }
        
        // Другой вариант для Double (синоним float)
        interpreter.addGlobalFunction("double") { args ->
            if (args.isNotEmpty()) {
                val value = when (args[0]) {
                    is ru.scripter.interpreter.StringValue -> {
                        try {
                            (args[0] as ru.scripter.interpreter.StringValue).value.toDouble()
                        } catch (e: Exception) {
                            0.0
                        }
                    }
                    is ru.scripter.interpreter.NumberValue -> {
                        (args[0] as ru.scripter.interpreter.NumberValue).value
                    }
                    else -> 0.0
                }
                _root_ide_package_.ru.scripter.interpreter.NumberValue(value)
            } else {
                _root_ide_package_.ru.scripter.interpreter.NumberValue(0.0)
            }
        }
        
        // Функция для конвертации в String
        interpreter.addGlobalFunction("str") { args ->
            if (args.isNotEmpty()) {
                _root_ide_package_.ru.scripter.interpreter.StringValue(args[0].toStringValue())
            } else {
                _root_ide_package_.ru.scripter.interpreter.StringValue("null")
            }
        }
        
        // Функция для получения типа значения
        interpreter.addGlobalFunction("typeof") { args ->
            if (args.isNotEmpty()) {
                val typeName = when (args[0]) {
                    is ru.scripter.interpreter.NumberValue -> {
                        val num = (args[0] as ru.scripter.interpreter.NumberValue).value
                        when {
                            num == num.toLong().toDouble() -> "Int"
                            else -> "Float"
                        }
                    }
                    is ru.scripter.interpreter.StringValue -> "String"
                    is ru.scripter.interpreter.BooleanValue -> "Boolean"
                    is ru.scripter.interpreter.ObjectValue -> "Object"
                    is ru.scripter.interpreter.FunctionValue -> "Function"
                    is ru.scripter.interpreter.NullValue -> "Null"
                    else -> "Unknown"
                }
                _root_ide_package_.ru.scripter.interpreter.StringValue(typeName)
            } else {
                _root_ide_package_.ru.scripter.interpreter.StringValue("Unknown")
            }
        }
        
        // Функция для форматирования числа с указанием типа
        interpreter.addGlobalFunction("format") { args ->
            if (args.isNotEmpty()) {
                val value = args[0]
                val valueStr = value.toStringValue()
                val type = when (value) {
                    is ru.scripter.interpreter.NumberValue -> {
                        val num = value.value
                        when {
                            num == num.toLong().toDouble() -> "Int"
                            else -> "Float"
                        }
                    }
                    is ru.scripter.interpreter.StringValue -> "String"
                    else -> "Unknown"
                }
                _root_ide_package_.ru.scripter.interpreter.StringValue("$valueStr [$type]")
            } else {
                _root_ide_package_.ru.scripter.interpreter.StringValue("null [Null]")
            }
        }
        
        interpreter.interpret(program)
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}