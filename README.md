# StoryScript

Интерпретатор для кастомного языка программирования **StoryScript**, написанный на Kotlin. Позволяет выполнять скрипты с поддержкой функций, переменных, управления потоком и интеграции с Java API.

## Возможности

- **Функции** — определение и вызов функций
- **Переменные** — динамическая типизация
- **Операторы** — арифметические, логические, сравнения
- **Управление потоком** — if/else, while, for, switch
- **Блоки кода** — lambda-подобные блоки с поддержкой `ActiveRunnable`
- **Интеграция с Java** — вызов методов Java объектов
- **Встроенные функции** — `entity()` конструктор для создания объектов

## Использование

### Установка и запуск

```bash
./gradlew build
./gradlew run --args="script.st"
```

### Пример скрипта

```kotlin
function main() {
    var test = entity(1)
    world.send(test.send_return())
    
    world.time(2) {
        world.send("Hello", "World")
    }
}
```

## Структура проекта

```
src/main/kotlin/
├── Main.kt                 — точка входа
├── org/example/
│   ├── lexer/              — лексический анализ
│   ├── parser/             — парсер AST
│   ├── interpreter/        — интерпретатор
│   └── cs/                 — встроенные API
```

## Синтаксис

### Функции
```kotlin
function greet(name) {
    world.send("Hello", name)
}
```

### Переменные и циклы
```kotlin
var counter = 0
while (counter < 10) {
    counter = counter + 1
}
```

### Таймер с блоком
```kotlin
world.time(5) {
    world.send("test", "message")
}
```

## Лицензия

MIT License — см. файл [LICENSE](LICENSE)

## Автор

StoryScript Interpreter
