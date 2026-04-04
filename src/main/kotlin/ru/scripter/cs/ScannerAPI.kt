package ru.scripter.cs

import java.util.Scanner

class ScannerAPI {
    private val scanner = Scanner(System.`in`)
    
    fun readLine(prompt: String = ""): String {
        if (prompt.isNotEmpty()) {
            print(prompt)
        }
        return scanner.nextLine()
    }
    
    fun readInt(prompt: String = ""): Int {
        if (prompt.isNotEmpty()) {
            print(prompt)
        }
        return try {
            scanner.nextLine().toInt()
        } catch (e: Exception) {
            0
        }
    }
    
    fun readDouble(prompt: String = ""): Double {
        if (prompt.isNotEmpty()) {
            print(prompt)
        }
        return try {
            scanner.nextLine().toDouble()
        } catch (e: Exception) {
            0.0
        }
    }
}