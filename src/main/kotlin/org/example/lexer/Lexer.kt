package org.example.lexer

// ============== Lexer ==============
// Лексер преобразует исходный код в список токенов
class Lexer(private val input: String) {
    private var position = 0
    private var line = 1
    private var column = 1
    
    private val keywords = mapOf(
        "function" to TokenType.FUNCTION,
        "var" to TokenType.VAR,
        "if" to TokenType.IF,
        "else" to TokenType.ELSE,
        "switch" to TokenType.SWITCH,
        "case" to TokenType.CASE,
        "break" to TokenType.BREAK,
        "default" to TokenType.DEFAULT,
        "for" to TokenType.FOR,
        "while" to TokenType.WHILE,
        "return" to TokenType.RETURN,
        "true" to TokenType.BOOLEAN,
        "false" to TokenType.BOOLEAN
    )
    
    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()
        
        while (position < input.length) {
            skipWhitespace()
            
            if (position >= input.length) break
            
            val currentChar = input[position]
            
            // Comments: // (C-style only)
            if (currentChar == '/' && peek() == '/') {
                skipComment()
                continue
            }
            
            // Numbers
            if (currentChar.isDigit()) {
                tokens.add(scanNumber())
                continue
            }
            
            // Strings: " или '
            if (currentChar == '"' || currentChar == '\'') {
                tokens.add(scanString())
                continue
            }
            
            // Identifiers and Keywords
            if (currentChar.isLetter() || currentChar == '_') {
                tokens.add(scanIdentifier())
                continue
            }
            
            // Operators and Punctuation
            val token = scanOperator()
            if (token != null) {
                tokens.add(token)
                continue
            }
            
            throw RuntimeException("Unexpected character: '$currentChar' at line $line, column $column")
        }
        
        tokens.add(Token(TokenType.EOF, "", line, column))
        return tokens
    }
    
    private fun skipWhitespace() {
        while (position < input.length && input[position].isWhitespace()) {
            if (input[position] == '\n') {
                line++
                column = 1
            } else {
                column++
            }
            position++
        }
    }
    
    private fun skipComment() {
        while (position < input.length && input[position] != '\n') {
            position++
            column++
        }
    }
    
    private fun scanNumber(): Token {
        val start = position
        val startCol = column
        
        while (position < input.length && (input[position].isDigit() || input[position] == '.')) {
            position++
            column++
        }
        
        return Token(TokenType.NUMBER, input.substring(start, position), line, startCol)
    }
    
    private fun scanString(): Token {
        val quote = input[position]
        position++
        column++
        
        val startCol = column - 1
        val sb = StringBuilder()
        
        while (position < input.length && input[position] != quote) {
            if (input[position] == '\\' && position + 1 < input.length) {
                position++
                column++
                when (input[position]) {
                    'n' -> sb.append('\n')
                    't' -> sb.append('\t')
                    'r' -> sb.append('\r')
                    '\\' -> sb.append('\\')
                    '"' -> sb.append('"')
                    '\'' -> sb.append('\'')
                    else -> sb.append(input[position])
                }
            } else {
                sb.append(input[position])
            }
            position++
            column++
        }
        
        if (position < input.length) {
            position++
            column++
        }
        
        return Token(TokenType.STRING, sb.toString(), line, startCol)
    }
    
    private fun scanIdentifier(): Token {
        val start = position
        val startCol = column
        
        while (position < input.length && (input[position].isLetterOrDigit() || input[position] == '_')) {
            position++
            column++
        }
        
        val value = input.substring(start, position)
        val type = keywords[value] ?: TokenType.IDENTIFIER
        
        return Token(type, value, line, startCol)
    }
    
    private fun scanOperator(): Token? {
        val startCol = column
        val c = input[position]
        
        return when {
            c == '+' && peek() == '+' -> {
                position += 2
                column += 2
                Token(TokenType.INCREMENT, "++", line, startCol)
            }
            c == '-' && peek() == '-' && position + 2 < input.length && input[position + 2] != '-' -> {
                position += 2
                column += 2
                Token(TokenType.DECREMENT, "--", line, startCol)
            }
            c == '*' && peek() == '*' -> {
                position += 2
                column += 2
                Token(TokenType.POWER, "**", line, startCol)
            }
            c == '/' && peek() == '/' -> {
                position += 2
                column += 2
                Token(TokenType.DOUBLE_SLASH, "//", line, startCol)
            }
            c == '=' && peek() == '=' && position + 2 < input.length && input[position + 2] == '=' -> {
                position += 3
                column += 3
                Token(TokenType.STRICT_EQUAL, "===", line, startCol)
            }
            c == '=' && peek() == '=' -> {
                position += 2
                column += 2
                Token(TokenType.EQUAL, "==", line, startCol)
            }
            c == '!' && peek() == '=' && position + 2 < input.length && input[position + 2] == '=' -> {
                position += 3
                column += 3
                Token(TokenType.NOT_STRICT_EQUAL, "!==", line, startCol)
            }
            c == '!' && peek() == '=' -> {
                position += 2
                column += 2
                Token(TokenType.NOT_EQUAL, "!=", line, startCol)
            }
            c == '<' && peek() == '=' -> {
                position += 2
                column += 2
                Token(TokenType.LESS_EQUAL, "<=", line, startCol)
            }
            c == '>' && peek() == '=' -> {
                position += 2
                column += 2
                Token(TokenType.GREATER_EQUAL, ">=", line, startCol)
            }
            c == '&' && peek() == '&' -> {
                position += 2
                column += 2
                Token(TokenType.AND, "&&", line, startCol)
            }
            c == '|' && peek() == '|' -> {
                position += 2
                column += 2
                Token(TokenType.OR, "||", line, startCol)
            }
            c == '+' && peek() == '=' -> {
                position += 2
                column += 2
                Token(TokenType.PLUS_ASSIGN, "+=", line, startCol)
            }
            c == '-' && peek() == '=' -> {
                position += 2
                column += 2
                Token(TokenType.MINUS_ASSIGN, "-=", line, startCol)
            }
            c == '*' && peek() == '=' -> {
                position += 2
                column += 2
                Token(TokenType.STAR_ASSIGN, "*=", line, startCol)
            }
            c == '/' && peek() == '=' -> {
                position += 2
                column += 2
                Token(TokenType.SLASH_ASSIGN, "/=", line, startCol)
            }
            c == '%' && peek() == '=' -> {
                position += 2
                column += 2
                Token(TokenType.PERCENT_ASSIGN, "%=", line, startCol)
            }
            c == '*' && peek() == '*' && position + 2 < input.length && input[position + 2] == '=' -> {
                position += 3
                column += 3
                Token(TokenType.POWER_ASSIGN, "**=", line, startCol)
            }
            c == '+' -> {
                position++
                column++
                Token(TokenType.PLUS, "+", line, startCol)
            }
            c == '-' -> {
                position++
                column++
                Token(TokenType.MINUS, "-", line, startCol)
            }
            c == '*' -> {
                position++
                column++
                Token(TokenType.STAR, "*", line, startCol)
            }
            c == '/' -> {
                position++
                column++
                Token(TokenType.SLASH, "/", line, startCol)
            }
            c == '%' -> {
                position++
                column++
                Token(TokenType.PERCENT, "%", line, startCol)
            }
            c == '?' -> {
                position++
                column++
                Token(TokenType.QUESTION, "?", line, startCol)
            }
            c == '=' -> {
                position++
                column++
                Token(TokenType.ASSIGN, "=", line, startCol)
            }
            c == '<' -> {
                position++
                column++
                Token(TokenType.LESS_THAN, "<", line, startCol)
            }
            c == '>' -> {
                position++
                column++
                Token(TokenType.GREATER_THAN, ">", line, startCol)
            }
            c == '!' -> {
                position++
                column++
                Token(TokenType.NOT, "!", line, startCol)
            }
            c == '(' -> {
                position++
                column++
                Token(TokenType.LPAREN, "(", line, startCol)
            }
            c == ')' -> {
                position++
                column++
                Token(TokenType.RPAREN, ")", line, startCol)
            }
            c == '{' -> {
                position++
                column++
                Token(TokenType.LBRACE, "{", line, startCol)
            }
            c == '}' -> {
                position++
                column++
                Token(TokenType.RBRACE, "}", line, startCol)
            }
            c == '[' -> {
                position++
                column++
                Token(TokenType.LBRACKET, "[", line, startCol)
            }
            c == ']' -> {
                position++
                column++
                Token(TokenType.RBRACKET, "]", line, startCol)
            }
            c == ';' -> {
                position++
                column++
                Token(TokenType.SEMICOLON, ";", line, startCol)
            }
            c == ',' -> {
                position++
                column++
                Token(TokenType.COMMA, ",", line, startCol)
            }
            c == ':' -> {
                position++
                column++
                Token(TokenType.COLON, ":", line, startCol)
            }
            c == '.' -> {
                position++
                column++
                Token(TokenType.DOT, ".", line, startCol)
            }
            else -> null
        }
    }
    
    private fun peek(offset: Int = 1): Char? {
        val pos = position + offset
        return if (pos < input.length) input[pos] else null
    }
}
