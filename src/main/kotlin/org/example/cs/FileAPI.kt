package org.example.cs

import java.io.File

class FileAPI {
    // Чтение файла целиком
    fun readFile(path: String): String {
        return try {
            File(path).readText()
        } catch (e: Exception) {
            ""
        }
    }
    
    // Запись в файл
    fun writeFile(path: String, content: String): Boolean {
        return try {
            File(path).writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Добавление в конец файла
    fun appendFile(path: String, content: String): Boolean {
        return try {
            File(path).appendText(content)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Чтение файла по строкам (возвращается список)
    fun readLines(path: String): List<String> {
        return try {
            File(path).readLines()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Проверка существования файла
    fun exists(path: String): Boolean = File(path).exists()
    
    // Удаление файла
    fun delete(path: String): Boolean {
        return try {
            File(path).delete()
        } catch (e: Exception) {
            false
        }
    }
    
    // Получить размер файла в байтах
    fun size(path: String): Long {
        return try {
            File(path).length()
        } catch (e: Exception) {
            0
        }
    }
    
    // Проверка, является ли путь папкой
    fun isDirectory(path: String): Boolean = File(path).isDirectory
    
    // Проверка, является ли путь файлом
    fun isFile(path: String): Boolean = File(path).isFile
    
    // Список файлов в папке
    fun listFiles(path: String): List<String> {
        return try {
            File(path).listFiles()?.map { it.name } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Создание папки
    fun mkdir(path: String): Boolean {
        return try {
            File(path).mkdir()
        } catch (e: Exception) {
            false
        }
    }
    
    // Получить абсолютный путь
    fun getAbsolutePath(path: String): String {
        return try {
            File(path).absolutePath
        } catch (e: Exception) {
            ""
        }
    }
    
    // Получить имя файла
    fun getFileName(path: String): String {
        return try {
            File(path).name
        } catch (e: Exception) {
            ""
        }
    }
}
