package org.example.parser

import org.example.lexer.TokenType

// ============== AST Node Classes ==============
// Базовый класс для всех узлов AST
sealed class ASTNode

// Statements (Выражения и операторы)
data class Program(val statements: List<ASTNode>) : ASTNode()
data class VariableDeclaration(val name: String, val initializer: Expression?) : ASTNode()
data class FunctionDeclaration(val name: String, val params: List<String>, val body: BlockStatement) : ASTNode()
data class BlockStatement(val statements: List<ASTNode>) : ASTNode()
data class IfStatement(val condition: Expression, val thenBranch: ASTNode, val elseBranch: ASTNode?) : ASTNode()
data class WhileStatement(val condition: Expression, val body: ASTNode) : ASTNode()
data class ForStatement(val init: ASTNode?, val condition: Expression?, val update: Expression?, val body: ASTNode) : ASTNode()
data class SwitchStatement(val expr: Expression, val cases: List<CaseClause>, val defaultCase: ASTNode?) : ASTNode()
class CaseClause(val value: Expression?, val statements: List<ASTNode>)
data class ReturnStatement(val value: Expression?) : ASTNode()
data class ExpressionStatement(val expr: Expression) : ASTNode()
class BreakStatement : ASTNode()

// Expressions (Выражения)
sealed class Expression : ASTNode()
data class BinaryOp(val left: Expression, val operator: TokenType, val right: Expression) : Expression()
data class UnaryOp(val operator: TokenType, val operand: Expression, val isPostfix: Boolean = false) : Expression()
data class CallExpression(val callee: Expression, val args: List<Expression>, val trailingBlock: BlockExpression? = null) : Expression()
data class MemberExpression(val object_: Expression, val property: String) : Expression()
data class Identifier(val name: String) : Expression()
data class NumberLiteral(val value: Double) : Expression()
data class StringLiteral(val value: String) : Expression()
data class BooleanLiteral(val value: Boolean) : Expression()
data class BlockExpression(val statements: List<ASTNode>) : Expression()
