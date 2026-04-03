# StoryScript

Интерпретатор для кастомного языка программирования **StoryScript**, написанный на Kotlin. Позволяет выполнять скрипты с поддержкой функций, переменных, управления потоком и интеграции с Java API.

## Возможности

- **Функции** — определение и вызов функций с параметрами
- **Переменные** — динамическая типизация
- **Операторы** — арифметические, логические, сравнения, инкремент/декремент
- **Управление потоком** — if/else, while, for, switch, break, return
- **Блоки кода** — lambda-подобные блоки с поддержкой `ActiveRunnable`
- **Интеграция с Java** — вызов методов Java объектов с автоматической конвертацией типов
- **Встроенные функции** — `entity()` конструктор и другие
- **Встроенные API** — WorldAPI, EntityAPI

## Установка и запуск

```bash
# Сборка проекта
./gradlew build

# Запуск скрипта
./gradlew run --args="script.st"

# Или напрямую
java -jar build/libs/StoryScript.jar script.st
```

## Типы данных

StoryScript поддерживает следующие типы:

| Тип | Описание | Пример |
|-----|---------|---------|
| **Число** | Целые и дробные числа | `42`, `3.14`, `-5` |
| **Строка** | Текст в двойных кавычках | `"Hello"`, `"test"` |
| **Булево** | true или false | `true`, `false` |
| **null** | Нулевое значение | `null` |
| **Функция** | Определенная функция | `function foo() {}` |
| **Объект** | Java объект | `entity(1)`, `world` |

## Синтаксис

### 1. Переменные

```kotlin
// Объявление с инициализацией
var name = "John"
var age = 30
var active = true

// Переменные могут менять тип
var x = 5
x = "text"  // Допустимо
```

### 2. Функции

```kotlin
// Функция без параметров
function greet() {
    world.send("Hello!")
}

// Функция с параметрами
function add(a, b) {
    var result = a + b
    return result
}

// Функция с return
function getValue() {
    return 42
}

// Вызов функции
greet()
var sum = add(5, 3)  // sum = 8
```

### 3. Условные операторы

#### if/else
```kotlin
if (age > 18) {
    world.send("Adult")
} else if (age > 13) {
    world.send("Teenager")
} else {
    world.send("Child")
}
```

#### switch
```kotlin
switch (status) {
    case 1:
        world.send("Sleeping")
        break
    case 2:
        world.send("Working")
        break
    default:
        world.send("Unknown")
}
```

### 4. Циклы

#### while
```kotlin
var i = 0
while (i < 10) {
    world.send(i)
    i = i + 1
}
```

#### for
```kotlin
// Классический for
for (var i = 0; i < 5; i++) {
    world.send(i)
}

// For без инициализации
for (; i < 10; i++) {
    world.send(i)
}
```

#### break и continue
```kotlin
var counter = 0
while (true) {
    if (counter == 5) {
        break  // Выход из цикла
    }
    counter = counter + 1
}
```

### 5. Арифметические операторы

```kotlin
var a = 10
var b = 3

var add = a + b        // 13 (сложение)
var sub = a - b        // 7 (вычитание)
var mul = a * b        // 30 (умножение)
var div = a / b        // 3.333... (деление)
var floor_div = a // b // 3 (целое деление)
var mod = a % b        // 1 (остаток от деления)
var power = 2 ^ 3      // 8 (степень)

// Унарные операторы
var neg = -5           // -5
var pos = +5           // 5
```

### 6. Логические операторы

```kotlin
var x = true
var y = false

var and_result = x && y  // false (логическое И)
var or_result = x || y   // true (логическое ИЛИ)
var not_result = !x      // false (логическое НЕ)
```

### 7. Операторы сравнения

```kotlin
var a = 10
var b = 5

a == b   // false (равно)
a != b   // true (не равно)
a > b    // true (больше)
a < b    // false (меньше)
a >= b   // true (больше или равно)
a <= b   // false (меньше или равно)

// Строгое сравнение типов
5 === "5"   // false (разные типы)
5 == "5"    // true (преобразуется в числа)
```

### 8. Инкремент и декремент

```kotlin
var count = 5

count++     // count = 6 (постфиксный инкремент)
++count     // count = 7 (префиксный инкремент)
count--     // count = 6 (постфиксный декремент)
--count     // count = 5 (префиксный декремент)

// Используются в выражениях
var a = 5
var b = a++  // b = 5, a = 6
var c = ++a  // c = 7, a = 7
```

### 9. Присваивание

```kotlin
var x = 10
x = 20        // Переприсвоение
x = x + 5     // x = 25
```

### 10. Строковая конкатенация

```kotlin
var greeting = "Hello" + " " + "World"  // "Hello World"
var message = "Count: " + 42            // "Count: 42"
```

### 11. Вложенные блоки

```kotlin
function outer() {
    var x = 1
    function inner() {
        var y = 2
        world.send(x + y)  // 3
    }
    inner()
}
```

## Встроенные функции

### entity(value: Int)

Создаёт объект `EntityAPI` с указанным значением.

```kotlin
var entity = entity(5)
entity.send()           // Выведет: [entity] 5
var value = entity.send_return()  // value = 5
```

## Встроенные API

### WorldAPI

Глобальный объект `world` для вывода и управления временем.

#### Методы

##### `send(text: String)`
Выводит текст в консоль.
```kotlin
world.send("Hello, World!")
```
Вывод: `Hello, World!`

##### `send(name: String, text: String)`
Выводит текст с префиксом имени.
```kotlin
world.send("Player", "Hello")
```
Вывод: `[Player] Hello`

##### `time(timer: Int, runnable: ActiveRunnable)`
Ждёт указанное количество секунд, затем выполняет блок кода.
```kotlin
world.time(2) {
    world.send("2 seconds passed")
}
```

### EntityAPI

Объект сущности, создаётся через `entity(value)`.

#### Свойства

- `name_ai: String` — имя сущности (по умолчанию `"entity"`)
- `test: Int` — значение, переданное при создании

#### Методы

##### `send()`
Выводит информацию о сущности.
```kotlin
var e = entity(42)
e.send()  // Выведет: [entity] 42
```

##### `send_return(): Int`
Возвращает значение `test`.
```kotlin
var e = entity(10)
var result = e.send_return()  // result = 10
world.send(result)             // Выведет: 10
```

## Примеры программ

### Пример 1: Простой вывод

```kotlin
function main() {
    world.send("Welcome to StoryScript!")
}
```

### Пример 2: Работа с переменными и функциями

```kotlin
function main() {
    var counter = 0
    
    function increment() {
        counter = counter + 1
    }
    
    increment()
    increment()
    world.send(counter)  // Выведет: 2
}
```

### Пример 3: Таймер с блоком

```kotlin
function main() {
    world.send("Waiting...")
    world.time(3) {
        world.send("Done!")
    }
}
```

### Пример 4: Создание и использование объектов

```kotlin
function main() {
    var entity1 = entity(100)
    var entity2 = entity(200)
    
    world.send("Entity 1:")
    entity1.send()
    
    world.send("Entity 2:")
    entity2.send()
    
    var total = entity1.send_return() + entity2.send_return()
    world.send("Total: " + total)
}
```

### Пример 5: Цикл с условиями

```kotlin
function main() {
    for (var i = 1; i <= 5; i++) {
        if (i % 2 == 0) {
            world.send(i + " is even")
        } else {
            world.send(i + " is odd")
        }
    }
}
```

### Пример 6: While цикл

```kotlin
function main() {
    var count = 0
    while (count < 3) {
        world.send("Count: " + count)
        count = count + 1
    }
}
```

## Область видимости переменных

StoryScript использует лексическую область видимости:

```kotlin
function main() {
    var global = "outside"
    
    function inner() {
        var local = "inside"
        world.send(global)  // Допустимо: видно из outer scope
        world.send(local)   // Допустимо: определено здесь
    }
    
    inner()
    // world.send(local)  // Ошибка: local не видна здесь
}
```

## Автоматическая конвертация типов

При вызове Java методов типы автоматически конвертируются:

```kotlin
var entity = entity(42)
world.send(entity.send_return())  // Int конвертируется в String
```

| Исходный тип | Целевой тип | Результат |
|---|---|---|
| `Int` | `String` | `toString()` |
| `String` | `Int` | `toInt()` (или 0 если не число) |
| Любой | `Boolean` | 0 → false, остальное → true |

## Обработка ошибок

Интерпретатор выбрасывает ошибки при:
- Делении на ноль
- Обращении к неопределённой переменной
- Вызове несуществующего метода
- Неправильном количестве параметров функции

```kotlin
var x = 10 / 0  // RuntimeException: Division by zero
```

## Структура проекта

```
src/main/kotlin/
├── Main.kt                 — точка входа
├── org/example/
│   ├── lexer/
│   │   ├── Lexer.kt       — лексический анализ
│   │   └── TokenType.kt   — типы токенов
│   ├── parser/
│   │   ├── Parser.kt      — парсер AST
│   │   └── ASTNode.kt     — узлы синтаксического дерева
│   ├── interpreter/
│   │   ├── Interpreter.kt — интерпретатор
│   │   ├── Value.kt       — типы значений
│   │   ├── Environment.kt — окружение переменных
│   │   ├── Exceptions.kt  — исключения
│   │   └── ActiveRunnable.kt — функциональный интерфейс
│   └── cs/
│       ├── WorldAPI.kt    — глобальный объект мира
│       └── EntityAPI.kt   — API для сущностей
```

## Лицензия

GPL v3 — см. файл [LICENSE](LICENSE)

Это значит:
- ✅ Свободное использование
- ✅ Свободное изучение
- ❌ **Распределение только с исходным кодом**
- ❌ **Модификация требует GPL v3 лицензии на производные**

## Автор

StoryScript Interpreter — язык программирования для создания интерактивных скриптов
