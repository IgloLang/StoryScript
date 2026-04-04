package ru.scripter.interpreter

import ru.scripter.parser.*
import ru.scripter.lexer.TokenType
import kotlin.math.floor

// ============== Interpreter ==============
// Интерпретатор исполняет AST и вычисляет результаты
class Interpreter {
    private val globalEnv = _root_ide_package_.ru.scripter.interpreter.Environment()
    internal var currentEnv = globalEnv
    
    fun addGlobalObject(name: String, obj: Any) {
        globalEnv.define(name, _root_ide_package_.ru.scripter.interpreter.ObjectValue(obj))
    }
    
    fun addGlobalFunction(name: String, function: (List<ru.scripter.interpreter.Value>) -> ru.scripter.interpreter.Value) {
        globalEnv.define(name, _root_ide_package_.ru.scripter.interpreter.BuiltinFunction(function))
    }
    
    internal fun executeBlock(block: ru.scripter.parser.BlockStatement) {
        for (stmt in block.statements) {
            try {
                execute(stmt)
            } catch (e: ReturnException) {
                throw e
            }
        }
    }
    
    fun interpret(program: Program) {
        try {
            // Сначала выполняем все statements (определяют функции)
            for (statement in program.statements) {
                execute(statement)
            }
            
            // Потом ищем и вызываем main()
            try {
                val mainFunc = globalEnv.get("main")
                if (mainFunc is FunctionValue) {
                    val previousEnv = currentEnv
                    currentEnv = Environment(mainFunc.closure)
                    
                    try {
                        execute(mainFunc.body)
                    } catch (e: ReturnException) {
                        // Ловим return из main
                    } finally {
                        currentEnv = previousEnv
                    }
                } else {
                    throw RuntimeException("main is not a function")
                }
            } catch (e: RuntimeException) {
                if ("Undefined variable: main" in e.message.orEmpty()) {
                    println("Error: Function 'main()' not defined")
                } else {
                    throw e
                }
            }
        } catch (e: ReturnException) {
            throw RuntimeException("Return outside function")
        }
    }
    
    private fun execute(node: ASTNode): ru.scripter.interpreter.Value {
        return when (node) {
            is Program -> {
                var result: Value = NullValue
                for (stmt in node.statements) {
                    result = execute(stmt)
                }
                result
            }
            is VariableDeclaration -> {
                val value = node.initializer?.let { evaluate(it) } ?: ru.scripter.interpreter.NullValue
                currentEnv.define(node.name, value)
                ru.scripter.interpreter.NullValue
            }
            is FunctionDeclaration -> {
                val function = ru.scripter.interpreter.FunctionValue(node.params, node.body, currentEnv)
                currentEnv.define(node.name, function)
                ru.scripter.interpreter.NullValue
            }
            is BlockStatement -> {
                val previousEnv = currentEnv
                currentEnv = ru.scripter.interpreter.Environment(currentEnv)
                
                var result: ru.scripter.interpreter.Value = ru.scripter.interpreter.NullValue
                try {
                    for (stmt in node.statements) {
                        result = execute(stmt)
                    }
                } finally {
                    currentEnv = previousEnv
                }
                result
            }
            is ExpressionStatement -> evaluate(node.expr)
            is IfStatement -> {
                val condition = evaluate(node.condition).toBoolean()
                if (condition) {
                    execute(node.thenBranch)
                } else if (node.elseBranch != null) {
                    execute(node.elseBranch)
                }
                ru.scripter.interpreter.NullValue
            }
            is WhileStatement -> {
                var result: ru.scripter.interpreter.Value = ru.scripter.interpreter.NullValue
                try {
                    while (evaluate(node.condition).toBoolean()) {
                        try {
                            result = execute(node.body)
                        } catch (e: BreakException) {
                            break
                        }
                    }
                } catch (e: ReturnException) {
                    throw e
                }
                result
            }
            is ForStatement -> {
                val previousEnv = currentEnv
                currentEnv = ru.scripter.interpreter.Environment(currentEnv)
                
                var result: ru.scripter.interpreter.Value = ru.scripter.interpreter.NullValue
                try {
                    if (node.init != null) {
                        execute(node.init)
                    }
                    
                    while (node.condition == null || evaluate(node.condition).toBoolean()) {
                        try {
                            result = execute(node.body)
                        } catch (e: BreakException) {
                            break
                        }
                        
                        if (node.update != null) {
                            evaluate(node.update)
                        }
                    }
                } catch (e: ReturnException) {
                    throw e
                } finally {
                    currentEnv = previousEnv
                }
                result
            }
            is SwitchStatement -> {
                val expr = evaluate(node.expr)
                var matched = false
                var result: ru.scripter.interpreter.Value = ru.scripter.interpreter.NullValue
                
                try {
                    for (case in node.cases) {
                        if (matched || case.value == null || evaluate(case.value) == expr) {
                            matched = true
                            for (stmt in case.statements) {
                                result = execute(stmt)
                            }
                        }
                    }
                    
                    if (!matched && node.defaultCase != null) {
                        result = execute(node.defaultCase)
                    }
                } catch (e: BreakException) {
                    // Break out of switch
                }
                result
            }
            is ReturnStatement -> {
                val value = node.value?.let { evaluate(it) } ?: ru.scripter.interpreter.NullValue
                throw ru.scripter.interpreter.ReturnException(value)
            }
            is BreakStatement -> {
                throw ru.scripter.interpreter.BreakException()
            }
            else -> ru.scripter.interpreter.NullValue
        }
    }
    
    private fun evaluate(expr: Expression): ru.scripter.interpreter.Value {
        return when (expr) {
            is NumberLiteral -> NumberValue(expr.value)
            is StringLiteral -> StringValue(expr.value)
            is BooleanLiteral -> BooleanValue(expr.value)
            is Identifier -> currentEnv.get(expr.name)
            is BinaryOp -> evaluateBinaryOp(expr)
            is UnaryOp -> evaluateUnaryOp(expr)
            is TernaryOp -> evaluateTernaryOp(expr)
            is AssignmentOp -> evaluateAssignmentOp(expr)
            is CallExpression -> evaluateCall(expr)
            is NewExpression -> evaluateNew(expr)
            is MemberExpression -> evaluateMember(expr)
            is BlockExpression -> ru.scripter.interpreter.FunctionValue(emptyList(), BlockStatement(expr.statements), currentEnv)
            else -> ru.scripter.interpreter.NullValue
        }
    }
    
    private fun evaluateBinaryOp(expr: BinaryOp): ru.scripter.interpreter.Value {
        return when (expr.operator) {
            TokenType.ASSIGN -> {
                val value = evaluate(expr.right)
                if (expr.left is Identifier) {
                    if (currentEnv.exists(expr.left.name)) {
                        currentEnv.set(expr.left.name, value)
                    } else {
                        currentEnv.define(expr.left.name, value)
                    }
                }
                value
            }
            TokenType.PLUS -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)
                
                if (left is ru.scripter.interpreter.StringValue || right is ru.scripter.interpreter.StringValue) {
                    ru.scripter.interpreter.StringValue(left.toStringValue() + right.toStringValue())
                } else {
                    ru.scripter.interpreter.NumberValue(left.toNumber() + right.toNumber())
                }
            }
            TokenType.MINUS -> ru.scripter.interpreter.NumberValue(evaluate(expr.left).toNumber() - evaluate(expr.right).toNumber())
            TokenType.STAR -> ru.scripter.interpreter.NumberValue(evaluate(expr.left).toNumber() * evaluate(expr.right).toNumber())
            TokenType.SLASH -> {
                val right = evaluate(expr.right).toNumber()
                if (right == 0.0) throw RuntimeException("Division by zero")
                ru.scripter.interpreter.NumberValue(evaluate(expr.left).toNumber() / right)
            }
            TokenType.DOUBLE_SLASH -> {
                val right = evaluate(expr.right).toNumber()
                if (right == 0.0) throw RuntimeException("Division by zero")
                ru.scripter.interpreter.NumberValue(floor(evaluate(expr.left).toNumber() / right))
            }
            TokenType.POWER -> {
                val base = evaluate(expr.left).toNumber()
                val exp = evaluate(expr.right).toNumber()
                ru.scripter.interpreter.NumberValue(Math.pow(base, exp))
            }
            TokenType.PERCENT -> {
                val right = evaluate(expr.right).toNumber()
                if (right == 0.0) throw RuntimeException("Division by zero")
                ru.scripter.interpreter.NumberValue(evaluate(expr.left).toNumber() % right)
            }
            TokenType.EQUAL -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)
                ru.scripter.interpreter.BooleanValue(compare(left, right, false))
            }
            TokenType.NOT_EQUAL -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)
                ru.scripter.interpreter.BooleanValue(!compare(left, right, false))
            }
            TokenType.STRICT_EQUAL -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)
                ru.scripter.interpreter.BooleanValue(compare(left, right, true))
            }
            TokenType.NOT_STRICT_EQUAL -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)
                ru.scripter.interpreter.BooleanValue(!compare(left, right, true))
            }
            TokenType.LESS_THAN -> ru.scripter.interpreter.BooleanValue(evaluate(expr.left).toNumber() < evaluate(expr.right).toNumber())
            TokenType.LESS_EQUAL -> ru.scripter.interpreter.BooleanValue(evaluate(expr.left).toNumber() <= evaluate(expr.right).toNumber())
            TokenType.GREATER_THAN -> ru.scripter.interpreter.BooleanValue(evaluate(expr.left).toNumber() > evaluate(expr.right).toNumber())
            TokenType.GREATER_EQUAL -> ru.scripter.interpreter.BooleanValue(evaluate(expr.left).toNumber() >= evaluate(expr.right).toNumber())
            TokenType.AND -> {
                val left = evaluate(expr.left)
                if (!left.toBoolean()) return ru.scripter.interpreter.BooleanValue(false)
                val right = evaluate(expr.right)
                ru.scripter.interpreter.BooleanValue(right.toBoolean())
            }
            TokenType.OR -> {
                val left = evaluate(expr.left)
                if (left.toBoolean()) return ru.scripter.interpreter.BooleanValue(true)
                val right = evaluate(expr.right)
                ru.scripter.interpreter.BooleanValue(right.toBoolean())
            }
            else -> ru.scripter.interpreter.NullValue
        }
    }
    
    private fun evaluateUnaryOp(expr: UnaryOp): ru.scripter.interpreter.Value {
        return when (expr.operator) {
            TokenType.MINUS -> ru.scripter.interpreter.NumberValue(-evaluate(expr.operand).toNumber())
            TokenType.PLUS -> ru.scripter.interpreter.NumberValue(evaluate(expr.operand).toNumber())
            TokenType.NOT -> ru.scripter.interpreter.BooleanValue(!evaluate(expr.operand).toBoolean())
            TokenType.INCREMENT -> {
                if (expr.operand is Identifier) {
                    val value = currentEnv.get(expr.operand.name).toNumber() + 1
                    val result = ru.scripter.interpreter.NumberValue(value)
                    currentEnv.set(expr.operand.name, result)
                    if (expr.isPostfix) ru.scripter.interpreter.NumberValue(value - 1) else result
                } else {
                    throw RuntimeException("Invalid increment target")
                }
            }
            TokenType.DECREMENT -> {
                if (expr.operand is Identifier) {
                    val value = currentEnv.get(expr.operand.name).toNumber() - 1
                    val result = ru.scripter.interpreter.NumberValue(value)
                    currentEnv.set(expr.operand.name, result)
                    if (expr.isPostfix) ru.scripter.interpreter.NumberValue(value + 1) else result
                } else {
                    throw RuntimeException("Invalid decrement target")
                }
            }
            else -> ru.scripter.interpreter.NullValue
        }
    }
    
    private fun evaluateTernaryOp(expr: TernaryOp): ru.scripter.interpreter.Value {
        val condition = evaluate(expr.condition)
        return if (condition.toBoolean()) {
            evaluate(expr.thenExpr)
        } else {
            evaluate(expr.elseExpr)
        }
    }
    
    private fun evaluateAssignmentOp(expr: AssignmentOp): ru.scripter.interpreter.Value {
        val value = evaluate(expr.value)
        
        if (expr.target is Identifier) {
            val targetName = expr.target.name
            
            val finalValue = when (expr.operator) {
                TokenType.ASSIGN -> value
                TokenType.PLUS_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    ru.scripter.interpreter.NumberValue(current + value.toNumber())
                }
                TokenType.MINUS_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    ru.scripter.interpreter.NumberValue(current - value.toNumber())
                }
                TokenType.STAR_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    ru.scripter.interpreter.NumberValue(current * value.toNumber())
                }
                TokenType.SLASH_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    val divisor = value.toNumber()
                    if (divisor == 0.0) throw RuntimeException("Division by zero")
                    ru.scripter.interpreter.NumberValue(current / divisor)
                }
                TokenType.PERCENT_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    ru.scripter.interpreter.NumberValue(current % value.toNumber())
                }
                TokenType.POWER_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    ru.scripter.interpreter.NumberValue(Math.pow(current, value.toNumber()))
                }
                else -> value
            }
            
            if (currentEnv.exists(targetName)) {
                currentEnv.set(targetName, finalValue)
            } else {
                currentEnv.define(targetName, finalValue)
            }
            
            return finalValue
        } else if (expr.target is MemberExpression) {
            val obj = evaluate(expr.target.object_)
            val propertyName = expr.target.property
            
            if (obj is ru.scripter.interpreter.ObjectValue) {
                val finalValue = when (expr.operator) {
                    TokenType.ASSIGN -> value
                    TokenType.PLUS_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        ru.scripter.interpreter.NumberValue(current + value.toNumber())
                    }
                    TokenType.MINUS_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        ru.scripter.interpreter.NumberValue(current - value.toNumber())
                    }
                    TokenType.STAR_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        ru.scripter.interpreter.NumberValue(current * value.toNumber())
                    }
                    TokenType.SLASH_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        val divisor = value.toNumber()
                        if (divisor == 0.0) throw RuntimeException("Division by zero")
                        ru.scripter.interpreter.NumberValue(current / divisor)
                    }
                    TokenType.PERCENT_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        ru.scripter.interpreter.NumberValue(current % value.toNumber())
                    }
                    TokenType.POWER_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        ru.scripter.interpreter.NumberValue(Math.pow(current, value.toNumber()))
                    }
                    else -> value
                }
                return setObjectProperty(obj.obj, propertyName, finalValue)
            }
        }
        
        throw RuntimeException("Invalid assignment target")
    }
    
    private fun getObjectProperty(obj: Any, propertyName: String): ru.scripter.interpreter.Value {
        return try {
            val field = obj::class.java.getDeclaredField(propertyName)
            field.isAccessible = true
            val fieldValue = field.get(obj)
            when (fieldValue) {
                is Number -> ru.scripter.interpreter.NumberValue(fieldValue.toDouble())
                is String -> ru.scripter.interpreter.StringValue(fieldValue)
                is Boolean -> ru.scripter.interpreter.BooleanValue(fieldValue)
                null -> ru.scripter.interpreter.NullValue
                else -> ru.scripter.interpreter.ObjectValue(fieldValue)
            }
        } catch (e: Exception) {
            ru.scripter.interpreter.NullValue
        }
    }
    
    private fun setObjectProperty(obj: Any, propertyName: String, value: ru.scripter.interpreter.Value): ru.scripter.interpreter.Value {
        return try {
            val field = obj::class.java.getDeclaredField(propertyName)
            field.isAccessible = true
            val javaValue = when (value) {
                is ru.scripter.interpreter.NumberValue -> value.value
                is ru.scripter.interpreter.StringValue -> value.value
                is ru.scripter.interpreter.BooleanValue -> value.value
                else -> null
            }
            field.set(obj, javaValue)
            value
        } catch (e: Exception) {
            value
        }
    }
    
    private fun evaluateCall(expr: CallExpression): ru.scripter.interpreter.Value {
        if (expr.callee is MemberExpression) {
            val obj = evaluate(expr.callee.object_)
            val methodName = expr.callee.property
            
            if (obj is ru.scripter.interpreter.ObjectValue) {
                val args = expr.args.map { evaluate(it) }
                val block = expr.trailingBlock?.let { evaluate(it) as? ru.scripter.interpreter.FunctionValue }
                return callJavaMethod(obj.obj, methodName, args, block)
            }
        }
        
        val function = evaluate(expr.callee)
        
        // Handle builtin functions
        if (function is ru.scripter.interpreter.BuiltinFunction) {
            val args = expr.args.map { evaluate(it) }
            return function.function(args)
        }
        
        if (function !is ru.scripter.interpreter.FunctionValue) {
            throw RuntimeException("Not a function: ${expr.callee}")
        }
        
        if (function.params.size != expr.args.size) {
            throw RuntimeException("Expected ${function.params.size} arguments, got ${expr.args.size}")
        }
        
        val previousEnv = currentEnv
        currentEnv = ru.scripter.interpreter.Environment(function.closure)
        
        try {
            for ((param, arg) in function.params.zip(expr.args)) {
                val value = evaluate(arg)
                currentEnv.define(param, value)
            }
            
            try {
                execute(function.body)
                return ru.scripter.interpreter.NullValue
            } catch (e: ru.scripter.interpreter.ReturnException) {
                return e.value
            }
        } finally {
            currentEnv = previousEnv
        }
    }
    
    private fun evaluateNew(expr: NewExpression): ru.scripter.interpreter.Value {
        return when (expr.className) {
            "List" -> {
                ru.scripter.interpreter.ObjectValue(ru.scripter.cs.ListAPI())
            }
            "Map" -> {
                ru.scripter.interpreter.ObjectValue(ru.scripter.cs.Map())
            }
            "Set" -> {
                ru.scripter.interpreter.ObjectValue(ru.scripter.cs.Set())
            }
            "Stack" -> {
                ru.scripter.interpreter.ObjectValue(ru.scripter.cs.Stack())
            }
            "Queue" -> {
                ru.scripter.interpreter.ObjectValue(ru.scripter.cs.Queue())
            }
            "StringBuilder" -> {
                ru.scripter.interpreter.ObjectValue(ru.scripter.cs.StringBuilder())
            }
            "Counter" -> {
                ru.scripter.interpreter.ObjectValue(ru.scripter.cs.Counter())
            }
            "Pair" -> {
                val first = if (expr.args.isNotEmpty()) evaluate(expr.args[0]) else null
                val second = if (expr.args.size > 1) evaluate(expr.args[1]) else null
                val firstVal = when (first) {
                    is ru.scripter.interpreter.StringValue -> first.value
                    is ru.scripter.interpreter.NumberValue -> first.value
                    is ru.scripter.interpreter.BooleanValue -> first.value
                    else -> first?.toString()
                }
                val secondVal = when (second) {
                    is ru.scripter.interpreter.StringValue -> second.value
                    is ru.scripter.interpreter.NumberValue -> second.value
                    is ru.scripter.interpreter.BooleanValue -> second.value
                    else -> second?.toString()
                }
                ru.scripter.interpreter.ObjectValue(ru.scripter.cs.Pair(firstVal, secondVal))
            }
            else -> throw RuntimeException("Unknown class: ${expr.className}")
        }
    }
    
    private fun callJavaMethod(obj: Any, methodName: String, args: List<ru.scripter.interpreter.Value>, block: ru.scripter.interpreter.FunctionValue? = null): ru.scripter.interpreter.Value {
        // Build parameter types based on values and block
        val paramTypes = mutableListOf<Class<*>>()
        for (arg in args) {
            paramTypes.add(when (arg) {
                is ru.scripter.interpreter.NumberValue -> Int::class.java
                is ru.scripter.interpreter.StringValue -> String::class.java
                is ru.scripter.interpreter.BooleanValue -> Boolean::class.java
                else -> String::class.java
            })
        }
        
        // Add ActiveRunnable type if there's a block
        if (block != null) {
            paramTypes.add(ru.scripter.interpreter.ActiveRunnable::class.java)
        }
        
        // Find all methods with the given name (public methods)
        val allMethods = obj::class.java.methods.filter { it.name == methodName }
        
        // If no public methods found, try declared methods
        val methodsToCheck = if (allMethods.isNotEmpty()) allMethods else obj::class.java.declaredMethods.filter { it.name == methodName }
        
        // Filter methods by argument count (exact match first)
        val targetArgCount = args.size + (if (block != null) 1 else 0)
        val matchingMethods = methodsToCheck.filter { it.parameterCount == targetArgCount }
        
        // If no exact match, try methods with more parameters (for default values)
        // This handles Kotlin default parameters and Java varargs
        val candidateMethods = if (matchingMethods.isNotEmpty()) {
            matchingMethods
        } else {
            methodsToCheck.filter { it.parameterCount >= targetArgCount }
        }
        
        if (candidateMethods.isEmpty()) {
            throw NoSuchMethodException("$methodName with $args.size arguments not found")
        }
        
        // Pick the best matching method
        // Prefer methods with fewer parameters (closer match to what we're passing)
        var method = candidateMethods.minByOrNull { it.parameterCount } ?: candidateMethods[0]
        var bestScore = Int.MIN_VALUE
        
        for (candidate in candidateMethods) {
            val paramTypes = candidate.parameterTypes
            var score = 0
            
            // Prefer methods where the parameter count is closest to our arg count
            score -= Math.abs(candidate.parameterCount - args.size) * 1000
            
            // Score based on how well the types match
            val argCountToScore = minOf(args.size, candidate.parameterCount)
            for (i in 0 until argCountToScore) {
                val arg = args[i]
                val paramType = paramTypes[i]
                when {
                    paramType == String::class.java && arg is ru.scripter.interpreter.StringValue -> score += 100
                    paramType == Int::class.java && arg is ru.scripter.interpreter.NumberValue -> score += 100
                    paramType == Boolean::class.java && arg is ru.scripter.interpreter.BooleanValue -> score += 100
                    paramType == Any::class.java -> score += 50 // Generic Object match
                    paramType.isAssignableFrom(String::class.java) && arg is ru.scripter.interpreter.StringValue -> score += 75
                    else -> score += 0
                }
            }
            
            if (score > bestScore) {
                bestScore = score
                method = candidate
            }
        }
        
        // Prepare arguments based on what the found method expects
        val methodParams = method.parameterTypes
        val methodArgs = mutableListOf<Any?>()
        
        // Only prepare arguments for the parameters the method actually expects
        for ((i, arg) in args.withIndex()) {
            if (i >= methodParams.size) break  // Don't exceed method parameter count
            
            val targetType = methodParams[i]
            
            methodArgs.add(when {
                targetType == String::class.java -> {
                    when (arg) {
                        is ru.scripter.interpreter.NumberValue -> arg.value.toInt().toString()
                        is ru.scripter.interpreter.StringValue -> arg.value
                        is ru.scripter.interpreter.BooleanValue -> arg.value.toString()
                        else -> arg.toStringValue()
                    }
                }
                targetType == Int::class.java -> {
                    when (arg) {
                        is ru.scripter.interpreter.NumberValue -> arg.value.toInt()
                        is ru.scripter.interpreter.StringValue -> arg.value.toIntOrNull() ?: 0
                        else -> arg.toNumber().toInt()
                    }
                }
                targetType == Boolean::class.java -> {
                    arg.toBoolean()
                }
                targetType == Object::class.java -> {
                    // For Object type (Any?), pass the appropriate value
                    when (arg) {
                        is ru.scripter.interpreter.NumberValue -> arg.value.toInt()
                        is ru.scripter.interpreter.StringValue -> arg.value
                        is ru.scripter.interpreter.BooleanValue -> arg.value
                        else -> arg.toStringValue()
                    }
                }
                else -> {
                    when (arg) {
                        is ru.scripter.interpreter.NumberValue -> arg.value.toInt()
                        is ru.scripter.interpreter.StringValue -> arg.value
                        is ru.scripter.interpreter.BooleanValue -> arg.value
                        else -> arg.toStringValue()
                    }
                }
            })
        }
        
        // Add the block as ActiveRunnable if present
        if (block != null) {
            // Create a dynamic ActiveRunnable from the block
            val activeRunnable = ScriptActiveRunnable(block, this, currentEnv)
            methodArgs.add(activeRunnable)
        }
        
        val result = method.invoke(obj, *methodArgs.toTypedArray())
        return when (result) {
            is String -> ru.scripter.interpreter.StringValue(result)
            is Number -> ru.scripter.interpreter.NumberValue(result.toDouble())
            is Boolean -> ru.scripter.interpreter.BooleanValue(result)
            null -> ru.scripter.interpreter.NullValue
            else -> ru.scripter.interpreter.ObjectValue(result)
        }
    }
    
    private fun evaluateMember(expr: MemberExpression): ru.scripter.interpreter.Value {
        val obj = evaluate(expr.object_)
        
        if (obj is ru.scripter.interpreter.ObjectValue) {
            // Try to access as Kotlin property (getter method)
            val getterMethodName = "get${expr.property.replaceFirstChar { it.uppercase() }}"
            try {
                val getter = obj.obj::class.java.getMethod(getterMethodName)
                val value = getter.invoke(obj.obj)
                return when (value) {
                    is String -> ru.scripter.interpreter.StringValue(value)
                    is Number -> ru.scripter.interpreter.NumberValue(value.toDouble())
                    is Boolean -> ru.scripter.interpreter.BooleanValue(value)
                    null -> ru.scripter.interpreter.NullValue
                    else -> ru.scripter.interpreter.ObjectValue(value)
                }
            } catch (e: NoSuchMethodException) {
                // Not a getter, try as property
            }
            
            // Try to access as direct property/field
            return try {
                val field = obj.obj::class.java.getField(expr.property)
                val value = field.get(obj.obj)
                when (value) {
                    is String -> ru.scripter.interpreter.StringValue(value)
                    is Number -> ru.scripter.interpreter.NumberValue(value.toDouble())
                    is Boolean -> ru.scripter.interpreter.BooleanValue(value)
                    null -> ru.scripter.interpreter.NullValue
                    else -> ru.scripter.interpreter.ObjectValue(value)
                }
            } catch (e: NoSuchFieldException) {
                ru.scripter.interpreter.NullValue
            }
        }
        
        return ru.scripter.interpreter.NullValue
    }
    
    private fun compare(left: ru.scripter.interpreter.Value, right: ru.scripter.interpreter.Value, strict: Boolean): Boolean {
        if (strict) {
            return when {
                left is ru.scripter.interpreter.NumberValue && right is ru.scripter.interpreter.NumberValue -> left.value == right.value
                left is ru.scripter.interpreter.StringValue && right is ru.scripter.interpreter.StringValue -> left.value == right.value
                left is ru.scripter.interpreter.BooleanValue && right is ru.scripter.interpreter.BooleanValue -> left.value == right.value
                left is ru.scripter.interpreter.NullValue && right is ru.scripter.interpreter.NullValue -> true
                else -> false
            }
        } else {
            // Loose comparison
            return when {
                left is ru.scripter.interpreter.NumberValue && right is ru.scripter.interpreter.NumberValue -> left.value == right.value
                left is ru.scripter.interpreter.StringValue && right is ru.scripter.interpreter.StringValue -> left.value == right.value
                left is ru.scripter.interpreter.BooleanValue && right is ru.scripter.interpreter.BooleanValue -> left.value == right.value
                left is ru.scripter.interpreter.NullValue && right is ru.scripter.interpreter.NullValue -> true
                else -> left.toNumber() == right.toNumber()
            }
        }
    }
}
