package ru.scripter.interpreter

// ============== Exceptions for Control Flow ==============
// Исключения для управления потоком выполнения (break, return)

class BreakException : Exception()
class ReturnException(val value: ru.scripter.interpreter.Value) : Exception()
