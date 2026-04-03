package org.example.parser

import org.example.lexer.Token
import org.example.lexer.TokenType

// ============== Parser ==============
// Парсер преобразует список токенов в AST (абстрактное синтаксическое дерево)
class Parser(private val tokens: List<Token>) {
    private var position = 0
    
    fun parse(): Program {
        val statements = mutableListOf<ASTNode>()
        
        while (!isAtEnd()) {
            skipSemicolons()
            if (!isAtEnd()) {
                val stmt = parseStatement()
                if (stmt != null) {
                    statements.add(stmt)
                }
            }
        }
        
        return Program(statements)
    }
    
    private fun parseStatement(): ASTNode? {
        return when {
            match(TokenType.VAR) -> parseVariableDeclaration()
            match(TokenType.FUNCTION) -> parseFunctionDeclaration()
            match(TokenType.IF) -> parseIfStatement()
            match(TokenType.WHILE) -> parseWhileStatement()
            match(TokenType.FOR) -> parseForStatement()
            match(TokenType.SWITCH) -> parseSwitchStatement()
            match(TokenType.RETURN) -> parseReturnStatement()
            match(TokenType.BREAK) -> {
                consumeSemicolon()
                BreakStatement()
            }
            match(TokenType.LBRACE) -> parseBlockStatement()
            else -> {
                val expr = parseExpression()
                consumeSemicolon()
                ExpressionStatement(expr)
            }
        }
    }
    
    private fun parseVariableDeclaration(): VariableDeclaration {
        val name = consume(TokenType.IDENTIFIER, "Expected variable name").value
        
        val initializer = if (match(TokenType.ASSIGN)) {
            parseExpression()
        } else {
            null
        }
        
        consumeSemicolon()
        return VariableDeclaration(name, initializer)
    }
    
    private fun parseFunctionDeclaration(): FunctionDeclaration {
        val name = consume(TokenType.IDENTIFIER, "Expected function name").value
        
        consume(TokenType.LPAREN, "Expected '(' after function name")
        val params = mutableListOf<String>()
        
        if (!check(TokenType.RPAREN)) {
            do {
                params.add(consume(TokenType.IDENTIFIER, "Expected parameter name").value)
            } while (match(TokenType.COMMA))
        }
        
        consume(TokenType.RPAREN, "Expected ')' after parameters")
        consume(TokenType.LBRACE, "Expected '{' for function body")
        
        val body = parseBlockStatement() as BlockStatement
        
        return FunctionDeclaration(name, params, body)
    }
    
    private fun parseIfStatement(): IfStatement {
        consume(TokenType.LPAREN, "Expected '(' after 'if'")
        val condition = parseExpression()
        consume(TokenType.RPAREN, "Expected ')' after condition")
        
        val thenBranch = parseStatement() ?: BlockStatement(emptyList())
        
        var elseBranch: ASTNode? = null
        if (match(TokenType.ELSE)) {
            elseBranch = parseStatement()
        }
        
        return IfStatement(condition, thenBranch, elseBranch)
    }
    
    private fun parseWhileStatement(): WhileStatement {
        consume(TokenType.LPAREN, "Expected '(' after 'while'")
        val condition = parseExpression()
        consume(TokenType.RPAREN, "Expected ')' after condition")
        
        val body = parseStatement() ?: BlockStatement(emptyList())
        
        return WhileStatement(condition, body)
    }
    
    private fun parseForStatement(): ForStatement {
        consume(TokenType.LPAREN, "Expected '(' after 'for'")
        
        val init = if (match(TokenType.SEMICOLON)) {
            null
        } else if (check(TokenType.VAR)) {
            advance()
            val name = consume(TokenType.IDENTIFIER, "Expected variable name").value
            val initializer = if (match(TokenType.ASSIGN)) parseExpression() else null
            consumeSemicolon()
            VariableDeclaration(name, initializer)
        } else {
            val expr = parseExpression()
            consumeSemicolon()
            ExpressionStatement(expr)
        }
        
        val condition = if (check(TokenType.SEMICOLON)) null else parseExpression()
        consume(TokenType.SEMICOLON, "Expected ';' after for condition")
        
        val update = if (check(TokenType.RPAREN)) null else parseExpression()
        consume(TokenType.RPAREN, "Expected ')' after for clauses")
        
        val body = parseStatement() ?: BlockStatement(emptyList())
        
        return ForStatement(init, condition, update, body)
    }
    
    private fun parseSwitchStatement(): SwitchStatement {
        consume(TokenType.LPAREN, "Expected '(' after 'switch'")
        val expr = parseExpression()
        consume(TokenType.RPAREN, "Expected ')' after switch expression")
        consume(TokenType.LBRACE, "Expected '{' for switch body")
        
        val cases = mutableListOf<CaseClause>()
        var defaultCase: ASTNode? = null
        
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            when {
                match(TokenType.CASE) -> {
                    val value = parseExpression()
                    consume(TokenType.COLON, "Expected ':' after case")
                    val statements = mutableListOf<ASTNode>()
                    
                    while (!check(TokenType.CASE) && !check(TokenType.DEFAULT) && !check(TokenType.RBRACE)) {
                        val stmt = parseStatement()
                        if (stmt != null) statements.add(stmt)
                    }
                    
                    cases.add(CaseClause(value, statements))
                }
                match(TokenType.DEFAULT) -> {
                    consume(TokenType.COLON, "Expected ':' after default")
                    val statements = mutableListOf<ASTNode>()
                    
                    while (!check(TokenType.CASE) && !check(TokenType.DEFAULT) && !check(TokenType.RBRACE)) {
                        val stmt = parseStatement()
                        if (stmt != null) statements.add(stmt)
                    }
                    
                    defaultCase = BlockStatement(statements)
                }
                else -> {
                    skipSemicolons()
                    if (!check(TokenType.RBRACE)) advance()
                }
            }
        }
        
        consume(TokenType.RBRACE, "Expected '}' after switch body")
        return SwitchStatement(expr, cases, defaultCase)
    }
    
    private fun parseReturnStatement(): ReturnStatement {
        val value = if (check(TokenType.SEMICOLON) || check(TokenType.RBRACE) || isAtEnd()) {
            null
        } else {
            parseExpression()
        }
        
        consumeSemicolon()
        return ReturnStatement(value)
    }
    
    private fun parseBlockStatement(): BlockStatement {
        val statements = mutableListOf<ASTNode>()
        
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            skipSemicolons()
            if (!check(TokenType.RBRACE)) {
                val stmt = parseStatement()
                if (stmt != null) {
                    statements.add(stmt)
                }
            }
        }
        
        consume(TokenType.RBRACE, "Expected '}'")
        return BlockStatement(statements)
    }
    
    private fun parseExpression(): Expression {
        return parseAssignment()
    }
    
    private fun parseAssignment(): Expression {
        var expr = parseTernary()
        
        if (match(TokenType.ASSIGN, TokenType.PLUS_ASSIGN, TokenType.MINUS_ASSIGN, 
                 TokenType.STAR_ASSIGN, TokenType.SLASH_ASSIGN, TokenType.PERCENT_ASSIGN, 
                 TokenType.POWER_ASSIGN)) {
            val operator = previous().type
            val value = parseAssignment()
            expr = AssignmentOp(expr, operator, value)
        }
        
        return expr
    }
    
    private fun parseTernary(): Expression {
        var expr = parseLogicalOr()
        
        if (match(TokenType.QUESTION)) {
            val thenExpr = parseExpression()
            consume(TokenType.COLON, "Expected ':' in ternary operator")
            val elseExpr = parseTernary()
            expr = TernaryOp(expr, thenExpr, elseExpr)
        }
        
        return expr
    }
    
    private fun parseLogicalOr(): Expression {
        var expr = parseLogicalAnd()
        
        while (match(TokenType.OR)) {
            val operator = previous().type
            val right = parseLogicalAnd()
            expr = BinaryOp(expr, operator, right)
        }
        
        return expr
    }
    
    private fun parseLogicalAnd(): Expression {
        var expr = parseEquality()
        
        while (match(TokenType.AND)) {
            val operator = previous().type
            val right = parseEquality()
            expr = BinaryOp(expr, operator, right)
        }
        
        return expr
    }
    
    private fun parseEquality(): Expression {
        var expr = parseComparison()
        
        while (match(TokenType.EQUAL, TokenType.NOT_EQUAL, TokenType.STRICT_EQUAL, TokenType.NOT_STRICT_EQUAL)) {
            val operator = previous().type
            val right = parseComparison()
            expr = BinaryOp(expr, operator, right)
        }
        
        return expr
    }
    
    private fun parseComparison(): Expression {
        var expr = parseAddition()
        
        while (match(TokenType.GREATER_THAN, TokenType.GREATER_EQUAL, TokenType.LESS_THAN, TokenType.LESS_EQUAL)) {
            val operator = previous().type
            val right = parseAddition()
            expr = BinaryOp(expr, operator, right)
        }
        
        return expr
    }
    
    private fun parseAddition(): Expression {
        var expr = parseMultiplication()
        
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            val operator = previous().type
            val right = parseMultiplication()
            expr = BinaryOp(expr, operator, right)
        }
        
        return expr
    }
    
    private fun parseMultiplication(): Expression {
        var expr = parsePower()
        
        while (match(TokenType.STAR, TokenType.SLASH, TokenType.DOUBLE_SLASH, TokenType.PERCENT)) {
            val operator = previous().type
            val right = parsePower()
            expr = BinaryOp(expr, operator, right)
        }
        
        return expr
    }
    
    private fun parsePower(): Expression {
        var expr = parseUnary()
        
        if (match(TokenType.POWER)) {
            val right = parsePower()  // Right associative
            expr = BinaryOp(expr, TokenType.POWER, right)
        }
        
        return expr
    }
    
    private fun parseUnary(): Expression {
        if (match(TokenType.NOT, TokenType.MINUS, TokenType.PLUS)) {
            val operator = previous().type
            val expr = parseUnary()
            return UnaryOp(operator, expr)
        }
        
        if (match(TokenType.NEW)) {
            val className = consume(TokenType.IDENTIFIER, "Expected class name after 'new'").value
            consume(TokenType.LPAREN, "Expected '(' after class name")
            val args = mutableListOf<Expression>()
            
            if (!check(TokenType.RPAREN)) {
                do {
                    args.add(parseExpression())
                } while (match(TokenType.COMMA))
            }
            
            consume(TokenType.RPAREN, "Expected ')' after arguments")
            return NewExpression(className, args)
        }
        
        if (match(TokenType.INCREMENT, TokenType.DECREMENT)) {
            val operator = previous().type
            val expr = parsePostfix()
            return UnaryOp(operator, expr)
        }
        
        return parsePostfix()
    }
    
    private fun parsePostfix(): Expression {
        var expr = parseCall()
        
        while (match(TokenType.INCREMENT, TokenType.DECREMENT)) {
            val operator = previous().type
            expr = UnaryOp(operator, expr, isPostfix = true)
        }
        
        return expr
    }
    
    private fun parseCall(): Expression {
        var expr = parseMember()
        
        while (match(TokenType.LPAREN)) {
            val args = mutableListOf<Expression>()
            
            if (!check(TokenType.RPAREN)) {
                do {
                    args.add(parseExpression())
                } while (match(TokenType.COMMA))
            }
            
            consume(TokenType.RPAREN, "Expected ')' after arguments")
            
            // Check for trailing block
            var trailingBlock: BlockExpression? = null
            if (check(TokenType.LBRACE)) {
                match(TokenType.LBRACE)
                val blockStatements = mutableListOf<ASTNode>()
                while (!check(TokenType.RBRACE) && !isAtEnd()) {
                    skipSemicolons()
                    if (!check(TokenType.RBRACE)) {
                        val stmt = parseStatement()
                        if (stmt != null) {
                            blockStatements.add(stmt)
                        }
                    }
                }
                consume(TokenType.RBRACE, "Expected '}' after block")
                trailingBlock = BlockExpression(blockStatements)
            }
            
            expr = CallExpression(expr, args, trailingBlock)
        }
        
        return expr
    }
    
    private fun parseMember(): Expression {
        var expr = parsePrimary()
        
        while (match(TokenType.DOT)) {
            val property = consume(TokenType.IDENTIFIER, "Expected property name").value
            expr = MemberExpression(expr, property)
        }
        
        return expr
    }
    
    private fun parsePrimary(): Expression {
        return when {
            match(TokenType.BOOLEAN) -> BooleanLiteral(previous().value == "true")
            match(TokenType.NUMBER) -> NumberLiteral(previous().value.toDouble())
            match(TokenType.STRING) -> StringLiteral(previous().value)
            match(TokenType.IDENTIFIER) -> Identifier(previous().value)
            match(TokenType.LPAREN) -> {
                val expr = parseExpression()
                consume(TokenType.RPAREN, "Expected ')' after expression")
                expr
            }
            else -> throw RuntimeException("Unexpected token: ${peek()}")
        }
    }
    
    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }
    
    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return false
        return peek().type == type
    }
    
    private fun advance(): Token {
        if (!isAtEnd()) position++
        return previous()
    }
    
    private fun isAtEnd(): Boolean {
        return peek().type == TokenType.EOF
    }
    
    private fun peek(): Token {
        return tokens[position]
    }
    
    private fun previous(): Token {
        return tokens[position - 1]
    }
    
    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw RuntimeException("$message at ${peek()}")
    }
    
    private fun consumeSemicolon() {
        if (check(TokenType.SEMICOLON)) {
            advance()
        } else if (check(TokenType.RBRACE) || check(TokenType.EOF)) {
            // Optional semicolon
        }
    }
    
    private fun skipSemicolons() {
        while (match(TokenType.SEMICOLON)) {
            // Skip
        }
    }
}
