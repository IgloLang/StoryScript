package org.example.lexer

// TokenType Enum - все типы токенов для лексера
enum class TokenType {
    // Keywords
    FUNCTION, VAR, IF, ELSE, SWITCH, CASE, BREAK, DEFAULT, FOR, WHILE, RETURN,
    
    // Identifiers and literals
    IDENTIFIER, NUMBER, STRING, BOOLEAN,
    
    // Operators
    PLUS, MINUS, STAR, SLASH, DOUBLE_SLASH, POWER, PERCENT,
    INCREMENT, DECREMENT,
    ASSIGN, EQUAL, STRICT_EQUAL, NOT_EQUAL, NOT_STRICT_EQUAL,
    LESS_THAN, GREATER_THAN, LESS_EQUAL, GREATER_EQUAL,
    AND, OR, NOT,
    
    // Punctuation
    LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET, RBRACKET,
    SEMICOLON, COMMA, COLON, DOT,
    
    // Special
    EOF, COMMENT
}
