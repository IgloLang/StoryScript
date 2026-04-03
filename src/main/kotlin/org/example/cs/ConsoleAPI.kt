package org.example.cs

class ConsoleAPI {
    // Вывод с цветом (простая реализация ANSI кодов)
    fun printRed(text: String) {
        println("\u001B[31m$text\u001B[0m")
    }
    
    fun printGreen(text: String) {
        println("\u001B[32m$text\u001B[0m")
    }
    
    fun printYellow(text: String) {
        println("\u001B[33m$text\u001B[0m")
    }
    
    fun printBlue(text: String) {
        println("\u001B[34m$text\u001B[0m")
    }
    
    fun printMagenta(text: String) {
        println("\u001B[35m$text\u001B[0m")
    }
    
    fun printCyan(text: String) {
        println("\u001B[36m$text\u001B[0m")
    }
    
    // Очистка экрана
    fun clear() {
        print("\u001B[2J\u001B[H")
    }
    
    // Вывод без новой строки
    fun print(text: String) {
        print(text)
    }
    
    // Вывод с новой строкой
    fun println(text: String) {
        println(text)
    }
    
    // Пустая строка
    fun newLine() {
        println()
    }
    
    // Повторение строки N раз
    fun repeat(text: String, times: Int) {
        for (i in 0 until times) {
            print(text)
        }
    }
    
    // Разделитель
    fun separator(width: Int = 50, char: String = "-") {
        for (i in 0 until width) {
            print(char)
        }
        println()
    }
    
    // Центрирование текста
    fun centerText(text: String, width: Int = 50) {
        val padding = (width - text.length) / 2
        for (i in 0 until padding) {
            print(" ")
        }
        println(text)
    }
}
