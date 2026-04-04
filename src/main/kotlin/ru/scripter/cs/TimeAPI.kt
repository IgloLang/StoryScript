package ru.scripter.cs

class TimeAPI {
    // Текущее время в миллисекундах
    fun currentTimeMillis(): Long = System.currentTimeMillis()
    
    // Текущее время в наносекундах
    fun nanoTime(): Long = System.nanoTime()
    
    // Получение текущего времени как строка
    fun now(): String = java.time.LocalDateTime.now().toString()
    
    // Получить только время (часы:минуты:секунды)
    fun time(): String {
        val now = java.time.LocalDateTime.now()
        return String.format("%02d:%02d:%02d", now.hour, now.minute, now.second)
    }
    
    // Получить только дату
    fun date(): String {
        val now = java.time.LocalDate.now()
        return now.toString()
    }
    
    // Получить день недели (0-6, где 0 - понедельник)
    fun dayOfWeek(): Int = java.time.LocalDate.now().dayOfWeek.value - 1
    
    // Получить день месяца
    fun dayOfMonth(): Int = java.time.LocalDate.now().dayOfMonth
    
    // Получить месяц (1-12)
    fun month(): Int = java.time.LocalDate.now().monthValue
    
    // Получить год
    fun year(): Int = java.time.LocalDate.now().year
    
    // Получить час (0-23)
    fun hour(): Int = java.time.LocalDateTime.now().hour
    
    // Получить минуты (0-59)
    fun minute(): Int = java.time.LocalDateTime.now().minute
    
    // Получить секунды (0-59)
    fun second(): Int = java.time.LocalDateTime.now().second
}
