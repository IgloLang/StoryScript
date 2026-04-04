package ru.scripter.cs

class StringAPI {
    // Получение длины строки
    fun length(str: String): Int = str.length
    
    fun len(str: String): Int = str.length
    
    // Подстрока
    fun substring(str: String, start: Int): String {
        return if (start >= 0 && start < str.length) str.substring(start) else ""
    }
    
    fun substring(str: String, start: Int, end: Int): String {
        return if (start >= 0 && end <= str.length && start <= end) str.substring(start, end) else ""
    }
    
    // Поиск
    fun indexOf(str: String, searchStr: String): Int = str.indexOf(searchStr)
    
    fun lastIndexOf(str: String, searchStr: String): Int = str.lastIndexOf(searchStr)
    
    fun contains(str: String, searchStr: String): Boolean = str.contains(searchStr)
    
    // Замена
    fun replace(str: String, oldStr: String, newStr: String): String = str.replace(oldStr, newStr)
    
    fun replaceFirst(str: String, oldStr: String, newStr: String): String = str.replaceFirst(oldStr, newStr)
    
    // Преобразование
    fun toUpperCase(str: String): String = str.uppercase()
    
    fun toLowerCase(str: String): String = str.lowercase()
    
    fun capitalize(str: String): String = str.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    
    // Очистка
    fun trim(str: String): String = str.trim()
    
    fun trimStart(str: String): String = str.trimStart()
    
    fun trimEnd(str: String): String = str.trimEnd()
    
    // Разделение и соединение
    fun split(str: String, delimiter: String): List<String> = str.split(delimiter)
    
    fun join(strings: List<String>, delimiter: String): String = strings.joinToString(delimiter)
    
    // Повтор
    fun repeat(str: String, times: Int): String = str.repeat(if (times > 0) times else 0)
    
    // Проверки
    fun startsWith(str: String, prefix: String): Boolean = str.startsWith(prefix)
    
    fun endsWith(str: String, suffix: String): Boolean = str.endsWith(suffix)
    
    fun isEmpty(str: String): Boolean = str.isEmpty()
    
    fun isBlank(str: String): Boolean = str.isBlank()
    
    // Символ по индексу
    fun charAt(str: String, index: Int): String {
        return if (index >= 0 && index < str.length) str[index].toString() else ""
    }
}
