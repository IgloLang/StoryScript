package org.example.interpreter

// ============== Exceptions for Control Flow ==============
// Исключения для управления потоком выполнения (break, return)

class BreakException : Exception()
class ReturnException(val value: org.example.interpreter.Value) : Exception()
