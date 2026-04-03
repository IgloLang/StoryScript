package org.example.lexer

// Token - представление одного токена в исходном коде
data class Token(
    val type: TokenType,
    val value: String,
    val line: Int,
    val column: Int
)
