package org.example.interpreter

import org.example.parser.*
import org.example.lexer.TokenType
import kotlin.math.floor

// ============== Interpreter ==============
// Интерпретатор исполняет AST и вычисляет результаты
class Interpreter {
    private val globalEnv = _root_ide_package_.org.example.interpreter.Environment()
    internal var currentEnv = globalEnv
    
    fun addGlobalObject(name: String, obj: Any) {
        globalEnv.define(name, _root_ide_package_.org.example.interpreter.ObjectValue(obj))
    }
    
    fun addGlobalFunction(name: String, function: (List<org.example.interpreter.Value>) -> org.example.interpreter.Value) {
        globalEnv.define(name, _root_ide_package_.org.example.interpreter.BuiltinFunction(function))
    }
    
    internal fun executeBlock(block: org.example.parser.BlockStatement) {
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
    
    private fun execute(node: ASTNode): Value {
        return when (node) {
            is Program -> {
                var result: Value = NullValue
                for (stmt in node.statements) {
                    result = execute(stmt)
                }
                result
            }
            is VariableDeclaration -> {
                val value = node.initializer?.let { evaluate(it) } ?: NullValue
                currentEnv.define(node.name, value)
                NullValue
            }
            is FunctionDeclaration -> {
                val function = FunctionValue(node.params, node.body, currentEnv)
                currentEnv.define(node.name, function)
                NullValue
            }
            is BlockStatement -> {
                val previousEnv = currentEnv
                currentEnv = Environment(currentEnv)
                
                var result: Value = NullValue
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
                NullValue
            }
            is WhileStatement -> {
                var result: Value = NullValue
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
                currentEnv = Environment(currentEnv)
                
                var result: Value = NullValue
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
                var result: Value = NullValue
                
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
                val value = node.value?.let { evaluate(it) } ?: NullValue
                throw ReturnException(value)
            }
            is BreakStatement -> {
                throw BreakException()
            }
            else -> NullValue
        }
    }
    
    private fun evaluate(expr: Expression): Value {
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
            is BlockExpression -> FunctionValue(emptyList(), BlockStatement(expr.statements), currentEnv)
            else -> NullValue
        }
    }
    
    private fun evaluateBinaryOp(expr: BinaryOp): Value {
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
                
                if (left is StringValue || right is StringValue) {
                    StringValue(left.toStringValue() + right.toStringValue())
                } else {
                    NumberValue(left.toNumber() + right.toNumber())
                }
            }
            TokenType.MINUS -> NumberValue(evaluate(expr.left).toNumber() - evaluate(expr.right).toNumber())
            TokenType.STAR -> NumberValue(evaluate(expr.left).toNumber() * evaluate(expr.right).toNumber())
            TokenType.SLASH -> {
                val right = evaluate(expr.right).toNumber()
                if (right == 0.0) throw RuntimeException("Division by zero")
                NumberValue(evaluate(expr.left).toNumber() / right)
            }
            TokenType.DOUBLE_SLASH -> {
                val right = evaluate(expr.right).toNumber()
                if (right == 0.0) throw RuntimeException("Division by zero")
                NumberValue(floor(evaluate(expr.left).toNumber() / right))
            }
            TokenType.POWER -> {
                val base = evaluate(expr.left).toNumber()
                val exp = evaluate(expr.right).toNumber()
                NumberValue(Math.pow(base, exp))
            }
            TokenType.PERCENT -> {
                val right = evaluate(expr.right).toNumber()
                if (right == 0.0) throw RuntimeException("Division by zero")
                NumberValue(evaluate(expr.left).toNumber() % right)
            }
            TokenType.EQUAL -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)
                BooleanValue(compare(left, right, false))
            }
            TokenType.NOT_EQUAL -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)
                BooleanValue(!compare(left, right, false))
            }
            TokenType.STRICT_EQUAL -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)
                BooleanValue(compare(left, right, true))
            }
            TokenType.NOT_STRICT_EQUAL -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)
                BooleanValue(!compare(left, right, true))
            }
            TokenType.LESS_THAN -> BooleanValue(evaluate(expr.left).toNumber() < evaluate(expr.right).toNumber())
            TokenType.LESS_EQUAL -> BooleanValue(evaluate(expr.left).toNumber() <= evaluate(expr.right).toNumber())
            TokenType.GREATER_THAN -> BooleanValue(evaluate(expr.left).toNumber() > evaluate(expr.right).toNumber())
            TokenType.GREATER_EQUAL -> BooleanValue(evaluate(expr.left).toNumber() >= evaluate(expr.right).toNumber())
            TokenType.AND -> {
                val left = evaluate(expr.left)
                if (!left.toBoolean()) return BooleanValue(false)
                val right = evaluate(expr.right)
                BooleanValue(right.toBoolean())
            }
            TokenType.OR -> {
                val left = evaluate(expr.left)
                if (left.toBoolean()) return BooleanValue(true)
                val right = evaluate(expr.right)
                BooleanValue(right.toBoolean())
            }
            else -> NullValue
        }
    }
    
    private fun evaluateUnaryOp(expr: UnaryOp): Value {
        return when (expr.operator) {
            TokenType.MINUS -> NumberValue(-evaluate(expr.operand).toNumber())
            TokenType.PLUS -> NumberValue(evaluate(expr.operand).toNumber())
            TokenType.NOT -> BooleanValue(!evaluate(expr.operand).toBoolean())
            TokenType.INCREMENT -> {
                if (expr.operand is Identifier) {
                    val value = currentEnv.get(expr.operand.name).toNumber() + 1
                    val result = NumberValue(value)
                    currentEnv.set(expr.operand.name, result)
                    if (expr.isPostfix) NumberValue(value - 1) else result
                } else {
                    throw RuntimeException("Invalid increment target")
                }
            }
            TokenType.DECREMENT -> {
                if (expr.operand is Identifier) {
                    val value = currentEnv.get(expr.operand.name).toNumber() - 1
                    val result = NumberValue(value)
                    currentEnv.set(expr.operand.name, result)
                    if (expr.isPostfix) NumberValue(value + 1) else result
                } else {
                    throw RuntimeException("Invalid decrement target")
                }
            }
            else -> NullValue
        }
    }
    
    private fun evaluateTernaryOp(expr: TernaryOp): Value {
        val condition = evaluate(expr.condition)
        return if (condition.toBoolean()) {
            evaluate(expr.thenExpr)
        } else {
            evaluate(expr.elseExpr)
        }
    }
    
    private fun evaluateAssignmentOp(expr: AssignmentOp): Value {
        val value = evaluate(expr.value)
        
        if (expr.target is Identifier) {
            val targetName = expr.target.name
            
            val finalValue = when (expr.operator) {
                TokenType.ASSIGN -> value
                TokenType.PLUS_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    NumberValue(current + value.toNumber())
                }
                TokenType.MINUS_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    NumberValue(current - value.toNumber())
                }
                TokenType.STAR_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    NumberValue(current * value.toNumber())
                }
                TokenType.SLASH_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    val divisor = value.toNumber()
                    if (divisor == 0.0) throw RuntimeException("Division by zero")
                    NumberValue(current / divisor)
                }
                TokenType.PERCENT_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    NumberValue(current % value.toNumber())
                }
                TokenType.POWER_ASSIGN -> {
                    val current = currentEnv.get(targetName).toNumber()
                    NumberValue(Math.pow(current, value.toNumber()))
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
            
            if (obj is ObjectValue) {
                val finalValue = when (expr.operator) {
                    TokenType.ASSIGN -> value
                    TokenType.PLUS_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        NumberValue(current + value.toNumber())
                    }
                    TokenType.MINUS_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        NumberValue(current - value.toNumber())
                    }
                    TokenType.STAR_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        NumberValue(current * value.toNumber())
                    }
                    TokenType.SLASH_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        val divisor = value.toNumber()
                        if (divisor == 0.0) throw RuntimeException("Division by zero")
                        NumberValue(current / divisor)
                    }
                    TokenType.PERCENT_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        NumberValue(current % value.toNumber())
                    }
                    TokenType.POWER_ASSIGN -> {
                        val current = getObjectProperty(obj.obj, propertyName).toNumber()
                        NumberValue(Math.pow(current, value.toNumber()))
                    }
                    else -> value
                }
                return setObjectProperty(obj.obj, propertyName, finalValue)
            }
        }
        
        throw RuntimeException("Invalid assignment target")
    }
    
    private fun getObjectProperty(obj: Any, propertyName: String): Value {
        return try {
            val field = obj::class.java.getDeclaredField(propertyName)
            field.isAccessible = true
            val fieldValue = field.get(obj)
            when (fieldValue) {
                is Number -> NumberValue(fieldValue.toDouble())
                is String -> StringValue(fieldValue)
                is Boolean -> BooleanValue(fieldValue)
                null -> NullValue
                else -> ObjectValue(fieldValue)
            }
        } catch (e: Exception) {
            NullValue
        }
    }
    
    private fun setObjectProperty(obj: Any, propertyName: String, value: Value): Value {
        return try {
            val field = obj::class.java.getDeclaredField(propertyName)
            field.isAccessible = true
            val javaValue = when (value) {
                is NumberValue -> value.value
                is StringValue -> value.value
                is BooleanValue -> value.value
                else -> null
            }
            field.set(obj, javaValue)
            value
        } catch (e: Exception) {
            value
        }
    }
    
    private fun evaluateCall(expr: CallExpression): Value {
        if (expr.callee is MemberExpression) {
            val obj = evaluate(expr.callee.object_)
            val methodName = expr.callee.property
            
            if (obj is ObjectValue) {
                val args = expr.args.map { evaluate(it) }
                val block = expr.trailingBlock?.let { evaluate(it) as? FunctionValue }
                return callJavaMethod(obj.obj, methodName, args, block)
            }
        }
        
        val function = evaluate(expr.callee)
        
        // Handle builtin functions
        if (function is BuiltinFunction) {
            val args = expr.args.map { evaluate(it) }
            return function.function(args)
        }
        
        if (function !is FunctionValue) {
            throw RuntimeException("Not a function: ${expr.callee}")
        }
        
        if (function.params.size != expr.args.size) {
            throw RuntimeException("Expected ${function.params.size} arguments, got ${expr.args.size}")
        }
        
        val previousEnv = currentEnv
        currentEnv = Environment(function.closure)
        
        try {
            for ((param, arg) in function.params.zip(expr.args)) {
                val value = evaluate(arg)
                currentEnv.define(param, value)
            }
            
            try {
                execute(function.body)
                return NullValue
            } catch (e: ReturnException) {
                return e.value
            }
        } finally {
            currentEnv = previousEnv
        }
    }
    
    private fun evaluateNew(expr: NewExpression): Value {
        return when (expr.className) {
            "List" -> {
                ObjectValue(org.example.cs.ListAPI())
            }
            "Map" -> {
                ObjectValue(org.example.cs.Map())
            }
            "Set" -> {
                ObjectValue(org.example.cs.Set())
            }
            "Stack" -> {
                ObjectValue(org.example.cs.Stack())
            }
            "Queue" -> {
                ObjectValue(org.example.cs.Queue())
            }
            "StringBuilder" -> {
                ObjectValue(org.example.cs.StringBuilder())
            }
            "Counter" -> {
                ObjectValue(org.example.cs.Counter())
            }
            "Pair" -> {
                val first = if (expr.args.isNotEmpty()) evaluate(expr.args[0]) else null
                val second = if (expr.args.size > 1) evaluate(expr.args[1]) else null
                val firstVal = when (first) {
                    is StringValue -> first.value
                    is NumberValue -> first.value
                    is BooleanValue -> first.value
                    else -> first?.toString()
                }
                val secondVal = when (second) {
                    is StringValue -> second.value
                    is NumberValue -> second.value
                    is BooleanValue -> second.value
                    else -> second?.toString()
                }
                ObjectValue(org.example.cs.Pair(firstVal, secondVal))
            }
            else -> throw RuntimeException("Unknown class: ${expr.className}")
        }
    }
    
    private fun callJavaMethod(obj: Any, methodName: String, args: List<Value>, block: FunctionValue? = null): Value {
        // Build parameter types based on values and block
        val paramTypes = mutableListOf<Class<*>>()
        for (arg in args) {
            paramTypes.add(when (arg) {
                is NumberValue -> Int::class.java
                is StringValue -> String::class.java
                is BooleanValue -> Boolean::class.java
                else -> String::class.java
            })
        }
        
        // Add ActiveRunnable type if there's a block
        if (block != null) {
            paramTypes.add(org.example.interpreter.ActiveRunnable::class.java)
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
                    paramType == String::class.java && arg is StringValue -> score += 100
                    paramType == Int::class.java && arg is NumberValue -> score += 100
                    paramType == Boolean::class.java && arg is BooleanValue -> score += 100
                    paramType == Any::class.java -> score += 50 // Generic Object match
                    paramType.isAssignableFrom(String::class.java) && arg is StringValue -> score += 75
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
                        is NumberValue -> arg.value.toInt().toString()
                        is StringValue -> arg.value
                        is BooleanValue -> arg.value.toString()
                        else -> arg.toStringValue()
                    }
                }
                targetType == Int::class.java -> {
                    when (arg) {
                        is NumberValue -> arg.value.toInt()
                        is StringValue -> arg.value.toIntOrNull() ?: 0
                        else -> arg.toNumber().toInt()
                    }
                }
                targetType == Boolean::class.java -> {
                    arg.toBoolean()
                }
                targetType == Object::class.java -> {
                    // For Object type (Any?), pass the appropriate value
                    when (arg) {
                        is NumberValue -> arg.value.toInt()
                        is StringValue -> arg.value
                        is BooleanValue -> arg.value
                        else -> arg.toStringValue()
                    }
                }
                else -> {
                    when (arg) {
                        is NumberValue -> arg.value.toInt()
                        is StringValue -> arg.value
                        is BooleanValue -> arg.value
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
            is String -> StringValue(result)
            is Number -> NumberValue(result.toDouble())
            is Boolean -> BooleanValue(result)
            null -> NullValue
            else -> ObjectValue(result)
        }
    }
    
    private fun evaluateMember(expr: MemberExpression): Value {
        val obj = evaluate(expr.object_)
        
        if (obj is ObjectValue) {
            // Try to access as Kotlin property (getter method)
            val getterMethodName = "get${expr.property.replaceFirstChar { it.uppercase() }}"
            try {
                val getter = obj.obj::class.java.getMethod(getterMethodName)
                val value = getter.invoke(obj.obj)
                return when (value) {
                    is String -> StringValue(value)
                    is Number -> NumberValue(value.toDouble())
                    is Boolean -> BooleanValue(value)
                    null -> NullValue
                    else -> ObjectValue(value)
                }
            } catch (e: NoSuchMethodException) {
                // Not a getter, try as property
            }
            
            // Try to access as direct property/field
            return try {
                val field = obj.obj::class.java.getField(expr.property)
                val value = field.get(obj.obj)
                when (value) {
                    is String -> StringValue(value)
                    is Number -> NumberValue(value.toDouble())
                    is Boolean -> BooleanValue(value)
                    null -> NullValue
                    else -> ObjectValue(value)
                }
            } catch (e: NoSuchFieldException) {
                NullValue
            }
        }
        
        return NullValue
    }
    
    private fun compare(left: Value, right: Value, strict: Boolean): Boolean {
        if (strict) {
            return when {
                left is NumberValue && right is NumberValue -> left.value == right.value
                left is StringValue && right is StringValue -> left.value == right.value
                left is BooleanValue && right is BooleanValue -> left.value == right.value
                left is NullValue && right is NullValue -> true
                else -> false
            }
        } else {
            // Loose comparison
            return when {
                left is NumberValue && right is NumberValue -> left.value == right.value
                left is StringValue && right is StringValue -> left.value == right.value
                left is BooleanValue && right is BooleanValue -> left.value == right.value
                left is NullValue && right is NullValue -> true
                else -> left.toNumber() == right.toNumber()
            }
        }
    }
}
