# 🎭 StoryScript

**Язык программирования для создания интерактивных скриптов**

Интерпретатор StoryScript позволяет выполнять скрипты с полной поддержкой функций, переменных, управления потоком и интеграции с Java объектами. Написан на **Kotlin** с использованием классического подхода: лексер → парсер → интерпретатор.

---

## ⚡ Быстрый старт

### Требования
- Java 11+
- Gradle (входит в проект)

### Установка и запуск

```bash
# Клонировать/открыть проект
cd StoryScript

# Сборка проекта
./gradlew build

# Запуск скрипта
./gradlew run --args="script.st"
```

### Первый скрипт

`script.st`:
```kotlin
function main() {
    world.send("Hello, StoryScript!")
}
```

Результат:
```
Hello, StoryScript!
```

---

## 📚 Типы данных

| Тип | Описание | Примеры |
|-----|---------|---------|
| **Number** | Int, Float | `42`, `3.14`, `-5` |
| **String** | Текст | `"Hello"`, `'Single quotes'` |
| **Boolean** | Истину/ложь | `true`, `false` |
| **null** | Нулевое значение | `null` |
| **Function** | Функция | `function foo() {}` |
| **Object** | Java объект | `entity(10)`, `world` |

---

## 🔤 Синтаксис и операторы

### Переменные

```kotlin
// Объявление
var name = "Alice"
var age = 25
var active = true
var value = null

// Динамическая типизация
var x = 10
x = "text"        // Допустимо, тип меняется
```

### Функции

```kotlin
// Определение функции
function greet(name) {
    world.send("Hello, " + name)
}

// С return
function add(a, b) {
    return a + b
}

// Без параметров
function sayHi() {
    world.send("Hi!")
}

// Вызов
greet("Bob")
var sum = add(5, 3)
```

### Условные операторы

#### if/else if/else
```kotlin
if (age < 13) {
    world.send("Child")
} else if (age < 18) {
    world.send("Teen")
} else {
    world.send("Adult")
}
```

#### switch/case
```kotlin
switch (status) {
    case 1:
        world.send("Idle")
        break
    case 2:
        world.send("Working")
        break
    default:
        world.send("Unknown")
}
```

### Циклы

#### while
```kotlin
var i = 0
while (i < 5) {
    world.send(i)
    i = i + 1
}
```

#### for
```kotlin
// Классический for с инициализацией
for (var i = 0; i < 5; i++) {
    world.send(i)
}

// В скобках: init; condition; update
for (var x = 10; x > 0; x--) {
    world.send(x)
}
```

#### break
```kotlin
while (true) {
    if (condition) {
        break  // Выход из цикла
    }
}
```

### Арифметические операторы

```kotlin
var a = 10
var b = 3

a + b        // 13 (сложение)
a - b        // 7 (вычитание)
a * b        // 30 (умножение)
a / b        // 3.333... (деление)
a // b       // 3 (целое деление)
a % b        // 1 (остаток)
2 ^ 3        // 8 (степень)

-a           // -10 (унарный минус)
+b           // 3 (унарный плюс)
```

### Логические операторы

```kotlin
true && false    // false (И)
true || false    // true (ИЛИ)
!true            // false (НЕ)

x && y           // Короткозамыкаемое И
x || y           // Короткозамыкаемое ИЛИ
```

### Операторы сравнения

```kotlin
5 == 5           // true (равно)
5 != 3           // true (не равно)
5 > 3            // true (больше)
5 < 3            // false (меньше)
5 >= 5           // true (≥)
5 <= 5           // true (≤)

5 === "5"        // false (строгое равно, учитывает тип)
5 == "5"         // true (мягкое равно, конвертирует)
```

### Инкремент/Декремент

```kotlin
var x = 5

x++              // 5 (постфиксный, вернёт старое значение)
++x              // 7 (префиксный, вернёт новое значение)
x--              // 7 (постфиксный)
--x              // 5 (префиксный)

// В циклах
for (var i = 0; i < 5; i++) { }
```

### Присваивание

```kotlin
var x = 10
x = 20           // Переприсвоение
x = x + 5        // x = 25
```

### Составные операторы присваивания

```kotlin
var x = 10

x += 5           // x = x + 5  → x = 15
x -= 3           // x = x - 3  → x = 12
x *= 2           // x = x * 2  → x = 24
x /= 4           // x = x / 4  → x = 6
x %= 2           // x = x % 2  → x = 0
x **= 3          // x = x ** 3 → x = 0
```

### Тернарный оператор

```kotlin
// condition ? значение_если_истина : значение_если_ложь
var age = 25
var status = age >= 18 ? "Adult" : "Minor"
world.send(status)  // Выведет "Adult"

// Вложенные тернарные операторы
var score = 85
var grade = score >= 90 ? "A" : score >= 80 ? "B" : score >= 70 ? "C" : "F"
world.send(grade)  // Выведет "B"
```

### Строковая конкатенация

```kotlin
"Hello" + " " + "World"   // "Hello World"
"Count: " + 42            // "Count: 42"
"Value: " + true          // "Value: true"
```

### Область видимости

```kotlin
function outer() {
    var x = 1              // Внешняя переменная
    
    function inner() {
        var y = 2          // Локальная переменная
        world.send(x)      // ✓ Можно обращаться к x
        world.send(y)      // ✓ Можно обращаться к y
    }
    
    inner()
    // world.send(y)       // ✗ Ошибка: y не видна здесь
}
```

---

## 🔌 Встроенные API

### WorldAPI (объект `world`)

Глобальный объект для вывода и управления временем.

#### `send(text: String)`
Выводит строку в консоль.
```kotlin
world.send("Hello")
// Вывод: Hello
```

#### `send(name: String, text: String)`
Выводит строку с префиксом (именем).
```kotlin
world.send("Player", "Action")
// Вывод: [Player] Action
```

#### `time(seconds: Int, block)`
Ждёт N секунд, затем выполняет блок кода (lambda).
```kotlin
world.send("Starting...")
world.time(3) {
    world.send("Finished!")
}
// После 3 секунд выведет: Finished!
```

### EntityAPI

Создаётся функцией `entity(value: Int)`.

```kotlin
var e = entity(42)
```

#### Свойства
- **`name_ai`** (String) — имя сущности, по умолчанию `"entity"`
- **`test`** (Int) — значение, переданное при создании

#### Методы

##### `send()`
Выводит информацию о сущности.
```kotlin
var e = entity(100)
e.send()
// Вывод: [entity] 100
```

##### `send_return(): Int`
Возвращает значение `test`.
```kotlin
var e = entity(42)
var value = e.send_return()    // value = 42
world.send(value)              // Вывод: 42
```

---

## 🎯 Встроенные функции

### `entity(value: Int): EntityAPI`

Конструктор для создания объектов со значением.

```kotlin
var entity1 = entity(10)
var entity2 = entity(20)

entity1.send()         // [entity] 10
entity2.send()         // [entity] 20
```

### `input(prompt: String): String`

Выводит сообщение и ждёт ввода строки от пользователя.

```kotlin
var name = input("Введите ваше имя: ")
world.send("Привет, " + name)
```

### `int(value): Int`

Конвертирует значение в целое число.

```kotlin
var numStr = "42"
var num = int(numStr)           // 42
var num2 = int(3.14)            // 3
var num3 = int(true)            // 1
var num4 = int(false)           // 0
```

### `float(value): Float` / `double(value): Double`

Конвертирует значение в число с плавающей точкой.

```kotlin
var floatNum = float("3.14")    // 3.14
var doubleNum = double(42)      // 42.0
var dbl = double("2.71828")     // 2.71828
```

### `str(value): String`

Конвертирует значение в строку.

```kotlin
var str1 = str(42)              // "42"
var str2 = str(3.14)            // "3.14"
var str3 = str(true)            // "true"
```

### `typeof(value): String`

Возвращает строку с типом значения. Помощь при отладке типов данных.

```kotlin
var num = 42
var floatNum = 3.14
var text = "hello"

world.send(typeof(num))         // "Int"
world.send(typeof(floatNum))    // "Float"
world.send(typeof(text))        // "String"
world.send(typeof(true))        // "Boolean"
world.send(typeof(null))        // "Null"
```

### `format(value): String`

Форматирует значение со указанием его типа в формате `значение [Тип]`.

```kotlin
var intVal = 42
var floatVal = 3.14
var strVal = "text"

world.send(format(intVal))      // "42 [Int]"
world.send(format(floatVal))    // "3.14 [Float]"
world.send(format(strVal))      // "text [String]"
```

---

## 📖 Примеры программ

### Пример 1: Вывод текста

```kotlin
function main() {
    world.send("Welcome to StoryScript!")
    world.send("Program", "Started")
}
```

**Результат:**
```
Welcome to StoryScript!
[Program] Started
```

### Пример 2: Работа с переменными

```kotlin
function main() {
    var counter = 0
    
    function increment() {
        counter = counter + 1
    }
    
    increment()
    increment()
    increment()
    
    world.send("Counter: " + counter)
}
```

**Результат:** `Counter: 3`

### Пример 3: Цикл с условиями

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

**Результат:**
```
1 is odd
2 is even
3 is odd
4 is even
5 is odd
```

### Пример 4: Работа с объектами

```kotlin
function main() {
    var player = entity(100)
    var enemy = entity(50)
    
    world.send("Status:")
    world.send("Player HP: " + player.send_return())
    world.send("Enemy HP: " + enemy.send_return())
    
    var total = player.send_return() + enemy.send_return()
    world.send("Total: " + total)
}
```

**Результат:**
```
Status:
Player HP: 100
Enemy HP: 50
Total: 150
```

### Пример 5: Таймер с блоком

```kotlin
function main() {
    world.send("Countdown...")
    
    world.time(2) {
        world.send("2 seconds passed!")
    }
}
```

**Результат:**
```
Countdown...
[ждёт 2 секунды]
2 seconds passed!
```

### Пример 6: While цикл

```kotlin
function main() {
    var count = 0
    while (count < 3) {
        world.send("Count: " + count)
        count++
    }
}
```

**Результат:**
```
Count: 0
Count: 1
Count: 2
```

### Пример 7: Составные операторы присваивания

```kotlin
function main() {
    var x = 100
    world.send("Начальное значение: " + x)
    
    x += 25
    world.send("После x += 25: " + x)
    
    x -= 10
    world.send("После x -= 10: " + x)
    
    x *= 2
    world.send("После x *= 2: " + x)
    
    x /= 5
    world.send("После x /= 5: " + x)
}
```

**Результат:**
```
Начальное значение: 100
После x += 25: 125
После x -= 10: 115
После x *= 2: 230
После x /= 5: 46
```

### Пример 8: Тернарный оператор

```kotlin
function main() {
    var scores = 75
    
    // Простой тернарный оператор
    var result = scores >= 60 ? "Passed" : "Failed"
    world.send("Result: " + result)
    
    // Вложенные тернарные операторы
    var grade = scores >= 90 ? "A" :
                scores >= 80 ? "B" :
                scores >= 70 ? "C" :
                scores >= 60 ? "D" : "F"
    
    world.send("Grade: " + grade)
    
    // Определение уровня сложности
    var level = 25
    var difficulty = level <= 10 ? "Easy" :
                     level <= 50 ? "Medium" :
                     level <= 100 ? "Hard" : "Extreme"
    
    world.send("Difficulty: " + difficulty)
}
```

**Результат:**
```
Result: Passed
Grade: C
Difficulty: Medium
```

### Пример 9: Комбинация новых операторов

```kotlin
function main() {
    var hp = 100
    var damage = 0
    
    // Получение урона с использованием составного оператора
    damage += 25
    hp -= damage
    
    world.send("After damage: HP = " + hp)
    
    // Определение статуса с использованием тернарного оператора
    var status = hp > 50 ? "Healthy" :
                 hp > 25 ? "Wounded" : "Critical"
    
    world.send("Status: " + status)
    
    // Использование тернарного оператора в составном присваивании
    var multiplier = status == "Healthy" ? 1 : 2
    hp *= multiplier  // Применяем бонус к HP
    
    world.send("Final HP: " + hp)
}
```

**Результат:**
```
After damage: HP = 75
Status: Wounded
Final HP: 150
```

### Пример 10: Ввод данных и конвертация типов

```kotlin
function main() {
    world.send("=== Калькулятор ===")
    
    // Ввод первого числа
    var num1Str = input("Введите первое число: ")
    var num1 = float(num1Str)
    
    // Ввод второго числа
    var num2Str = input("Введите второе число: ")
    var num2 = float(num2Str)
    
    // Вычисления
    var sum = num1 + num2
    var product = num1 * num2
    var difference = num1 - num2
    
    // Вывод результатов
    world.send("")
    world.send("Результаты:")
    world.send("Сумма: " + sum)
    world.send("Произведение: " + product)
    world.send("Разность: " + difference)
    
    // Конвертация в строку для вывода
    var resultStr = str(sum)
    world.send("Сумма как строка: " + resultStr)
}
```

**Пример работы:**
```
=== Калькулятор ===
Введите первое число: 15.5
Введите второе число: 3.2

Результаты:
Сумма: 18.7
Произведение: 49.6
Разность: 12.3
Сумма как строка: 18.7
```

### Пример 11: Работа с типами данных (Int, Float, Double, String)

```kotlin
function main() {
    world.send("=== Проверка типов данных ===")
    
    // Ввод разных типов
    var intValue = int(input("Введите целое число: "))
    var floatValue = float(input("Введите число с точкой (Float): "))
    var doubleValue = double(input("Введите число двойной точности (Double): "))
    var stringValue = input("Введите текст: ")
    
    world.send("")
    world.send("=== Введённые значения и их типы ===")
    
    // Вывод с форматированием и типами
    world.send("Int: " + format(intValue))
    world.send("Float: " + format(floatValue))
    world.send("Double: " + format(doubleValue))
    world.send("String: " + format(stringValue))
    
    // Проверка типов через typeof
    world.send("")
    world.send("=== Результаты typeof ===")
    world.send("typeof(intValue) = " + typeof(intValue))
    world.send("typeof(floatValue) = " + typeof(floatValue))
    world.send("typeof(doubleValue) = " + typeof(doubleValue))
    world.send("typeof(stringValue) = " + typeof(stringValue))
    
    // Вычисления с разными типами
    world.send("")
    world.send("=== Математические операции ===")
    var sum = intValue + floatValue
    var product = floatValue * doubleValue
    
    world.send("Int + Float = " + format(sum))
    world.send("Float * Double = " + format(product))
}
```

**Пример работы:**
```
=== Проверка типов данных ===
Введите целое число: 10
Введите число с точкой (Float): 3.5
Введите число двойной точности (Double): 2.71828
Введите текст: Hello

=== Введённые значения и их типы ===
Int: 10 [Int]
Float: 3.5 [Float]
Double: 2.71828 [Float]
String: Hello [String]

=== Результаты typeof ===
typeof(intValue) = Int
typeof(floatValue) = Float
typeof(doubleValue) = Float
typeof(stringValue) = String

=== Математические операции ===
Int + Float = 13.5 [Float]
Float * Double = 9.51398 [Float]
```

---

## ⚙️ Автоматическая конвертация типов

При вызове Java методов типы автоматически преобразуются:

```kotlin
var e = entity(42)
world.send(e.send_return())  // Int → String: "42"
```

| Источник | Цель | Результат |
|----------|------|-----------|
| `Int` | `String` | Конвертирует через `toString()` |
| `String` | `Int` | Парсит как число или `0` |
| Любой | `Boolean` | `0` → `false`, остальное → `true` |

---

## 🛑 Обработка ошибок

Интерпретатор генерирует ошибки при:

| Ошибка | Пример |
|--------|--------|
| Деление на ноль | `10 / 0` → `Division by zero` |
| Неопределённая переменная | `world.send(x)` (если `x` не определена) |
| Неправильное количество параметров | `add(5)` (если функция требует 2) |
| Несуществующий метод | `entity.undefined_method()` |

---

## 📁 Структура проекта

```
StoryScript/
├── build/                    # Скомпилированные файлы
├── gradle/                   # Gradle wrapper
├── src/
│   └── main/
│       └── kotlin/
│           ├── Main.kt
│           └── org/example/
│               ├── lexer/
│               │   ├── Lexer.kt         # Лексический анализ
│               │   └── TokenType.kt     # Типы токенов
│               ├── parser/
│               │   ├── Parser.kt        # Парсер AST
│               │   └── ASTNode.kt       # Узлы дерева
│               ├── interpreter/
│               │   ├── Interpreter.kt   # Исполнитель
│               │   ├── Value.kt         # Типы значений
│               │   ├── Environment.kt   # Окружение
│               │   ├── Exceptions.kt    # Исключения
│               │   └── ActiveRunnable.kt# Функциональный интерфейс
│               └── cs/
│                   ├── WorldAPI.kt      # Глобальный объект
│                   └── EntityAPI.kt     # API сущностей
├── script.st                # Пример скрипта
├── build.gradle.kts         # Конфигурация Gradle
├── README.md                # Этот файл
└── LICENSE                  # GPL v3 лицензия
```

---

## 🏗️ Как работает интерпретатор

```
script.st (исходный код)
    ↓
[Lexer]     → Лексический анализ → Токены
    ↓
[Parser]    → Синтаксический анализ → AST (Abstract Syntax Tree)
    ↓
[Interpreter] → Исполнение → Результат
```

1. **Lexer** — преобразует текст в токены (ключевые слова, операторы, идентификаторы)
2. **Parser** — строит дерево синтаксиса на основе токенов
3. **Interpreter** — исполняет дерево и вычисляет результаты

---

## 🔧 Руководство для разработчиков: Создание собственных API

### Основная идея

Вы можете создавать свои Java классы и использовать их из StoryScript скриптов. Интерпретатор автоматически конвертирует типы и вызывает методы.

### Шаг 1: Создание Java класса

Создайте обычный Kotlin/Java класс в папке `src/main/kotlin/org/example/cs/`:

**YourAPI.kt:**
```kotlin
package org.example.cs

class YourAPI {
    var name: String = "Default"
    var value: Int = 0
    
    fun send(message: String) {
        println("[YourAPI] $message")
    }
    
    fun calculate(a: Int, b: Int): Int {
        return a + b
    }
    
    fun getValue(): Int {
        return value
    }
}
```

### Шаг 2: Регистрация объекта в интерпретаторе

Отредактируйте `Main.kt` и добавьте ваш объект:

```kotlin
fun main(args: Array<String>) {
    // ... существующий код ...
    
    val interpreter = Interpreter()
    interpreter.addGlobalObject("world", WorldAPI())
    
    // Добавьте вашу API
    interpreter.addGlobalObject("api", YourAPI())
    
    // ... остальной код ...
}
```

### Шаг 3: Использование в скриптах

Теперь вы можете использовать объект в скриптах:

```kotlin
function main() {
    api.send("Hello from script!")
    var result = api.calculate(5, 3)
    world.send("Result: " + result)
}
```

---

## 📝 Примеры собственных API

### Пример 1: Simple Logger API

**LoggerAPI.kt:**
```kotlin
package org.example.cs

class LoggerAPI {
    private var logLevel: String = "INFO"
    
    fun info(message: String) {
        println("[INFO] $message")
    }
    
    fun warn(message: String) {
        println("[WARN] $message")
    }
    
    fun error(message: String) {
        println("[ERROR] $message")
    }
    
    fun setLevel(level: String) {
        logLevel = level
    }
    
    fun getLevel(): String {
        return logLevel
    }
}
```

**Использование:**
```kotlin
function main() {
    logger.setLevel("DEBUG")
    logger.info("Application started")
    logger.error("Something went wrong")
}
```

**Main.kt:**
```kotlin
interpreter.addGlobalObject("logger", LoggerAPI())
```

---

### Пример 2: Math API с несколькими методами

**MathAPI.kt:**
```kotlin
package org.example.cs

class MathAPI {
    fun sqrt(value: Double): Double {
        return Math.sqrt(value)
    }
    
    fun pow(base: Double, exp: Double): Double {
        return Math.pow(base, exp)
    }
    
    fun max(a: Int, b: Int): Int {
        return if (a > b) a else b
    }
    
    fun min(a: Int, b: Int): Int {
        return if (a < b) a else b
    }
    
    fun abs(value: Double): Double {
        return Math.abs(value)
    }
}
```

**Использование:**
```kotlin
function main() {
    var result1 = math.sqrt(16)
    world.send("sqrt(16) = " + result1)
    
    var result2 = math.max(10, 20)
    world.send("max(10, 20) = " + result2)
}
```

---

### Пример 3: Game Entity API с состоянием

**GameEntityAPI.kt:**
```kotlin
package org.example.cs

class GameEntityAPI(val id: Int) {
    var health: Int = 100
    var mana: Int = 50
    var level: Int = 1
    var name: String = "Entity_$id"
    
    fun takeDamage(damage: Int) {
        health = health - damage
        if (health < 0) health = 0
    }
    
    fun heal(amount: Int) {
        health = health + amount
        if (health > 100) health = 100
    }
    
    fun levelUp() {
        level = level + 1
        health = health + 10
        mana = mana + 5
    }
    
    fun getStatus(): String {
        return "[$name] HP: $health, Mana: $mana, Level: $level"
    }
    
    fun isAlive(): Boolean {
        return health > 0
    }
}
```

**Использование:**
```kotlin
function main() {
    var player = entity(1)
    player.takeDamage(20)
    world.send(player.getStatus())
    
    player.heal(10)
    world.send(player.getStatus())
    
    if (player.isAlive()) {
        world.send("Player still alive!")
    }
}
```

**Main.kt:**
```kotlin
interpreter.addGlobalFunction("entity") { args ->
    if (args.isNotEmpty() && args[0] is org.example.interpreter.NumberValue) {
        val id = (args[0] as org.example.interpreter.NumberValue).value.toInt()
        org.example.interpreter.ObjectValue(GameEntityAPI(id))
    } else {
        org.example.interpreter.NullValue
    }
}
```

---

## 🔄 Встроенные функции (Builtin Functions)

Вы можете создавать встроенные функции, которые вызываются как обычные функции, но реализованы на Kotlin.

### Создание встроенной функции

В `Main.kt`:

```kotlin
// Функция без параметров
interpreter.addGlobalFunction("random") { args ->
    org.example.interpreter.NumberValue(Math.random() * 100)
}

// Функция с параметрами
interpreter.addGlobalFunction("square") { args ->
    if (args.isNotEmpty() && args[0] is org.example.interpreter.NumberValue) {
        val value = (args[0] as org.example.interpreter.NumberValue).value
        org.example.interpreter.NumberValue(value * value)
    } else {
        org.example.interpreter.NullValue
    }
}

// Функция с несколькими параметрами
interpreter.addGlobalFunction("multiply") { args ->
    if (args.size >= 2 && 
        args[0] is org.example.interpreter.NumberValue &&
        args[1] is org.example.interpreter.NumberValue) {
        val a = (args[0] as org.example.interpreter.NumberValue).value
        val b = (args[1] as org.example.interpreter.NumberValue).value
        org.example.interpreter.NumberValue(a * b)
    } else {
        org.example.interpreter.NullValue
    }
}
```

**Использование в скриптах:**
```kotlin
function main() {
    var r = random()
    var s = square(5)
    var m = multiply(3, 7)
    
    world.send("Random: " + r)
    world.send("Square: " + s)
    world.send("Multiply: " + m)
}
```

---

## 🔀 Конвертация типов

Интерпретатор автоматически конвертирует типы между StoryScript и Java:

| StoryScript | Java | Пример |
|-------------|------|--------|
| `10` | `Int` | `api.setCount(10)` |
| `"text"` | `String` | `api.send("text")` |
| `true` | `Boolean` | `api.setState(true)` |
| `null` | `null` | `api.value` → `null` |

### Поддерживаемые типы методов

```kotlin
// ✅ Поддерживается
fun method1(value: Int) {}
fun method2(value: String) {}
fun method3(value: Boolean) {}
fun method4(): Int { return 42 }
fun method5(): String { return "text" }
fun method6(): Boolean { return true }

// ❌ Не поддерживается напрямую
fun method7(list: List<Int>) {}  // Нужна обработка
fun method8(obj: CustomClass) {} // Нужна регистрация
```

---

## 🎯 Best Practices

### 1. Простота интерфейса
Делайте методы простыми и понятными:

```kotlin
// ✅ Хорошо
fun takeDamage(amount: Int)
fun getHealth(): Int
fun isAlive(): Boolean

// ❌ Плохо
fun complexOperation(a: Int, b: Int, c: Int, d: Int, e: Int)
fun doMultipleThings()
```

### 2. Постоянство состояния
Изменяйте состояние объекта, не создавайте новые:

```kotlin
// ✅ Хорошо
fun takeDamage(amount: Int) {
    health = health - amount
}

// ❌ Плохо
fun takeDamage(amount: Int): GameEntity {
    return GameEntity(health - amount)
}
```

### 3. Возвращаемые значения
Возвращайте примитивные типы:

```kotlin
// ✅ Хорошо
fun getStatus(): String { return "..." }
fun getHealth(): Int { return health }

// ⚠️ Требует обработки
fun getEntity(): Entity { return Entity() }
```

### 4. Null безопасность
Используйте non-null типы:

```kotlin
// ✅ Хорошо
fun send(message: String)

// ⚠️ Может вызвать NPE
fun send(message: String?)
```

### 5. Документируйте API
Добавляйте комментарии:

```kotlin
class PlayerAPI {
    /**
     * Наносит урон персонажу
     * @param amount Количество урона (положительное число)
     */
    fun takeDamage(amount: Int) {
        health = health - amount
    }
    
    /**
     * Получить текущее здоровье
     * @return Текущее здоровье (0-100)
     */
    fun getHealth(): Int {
        return health
    }
}
```

---

## 🧪 Тестирование своего API

Создайте тестовый скрипт:

```kotlin
function main() {
    // Инициализация
    var obj = entity(1)
    world.send("Object created")
    
    // Тестирование методов
    obj.takeDamage(10)
    world.send("Health after damage: " + obj.getHealth())
    
    obj.heal(5)
    world.send("Health after healing: " + obj.getHealth())
    
    // Проверка состояния
    if (obj.isAlive()) {
        world.send("Entity is alive")
    } else {
        world.send("Entity is dead")
    }
}
```

---

## 📋 Чек-лист для добавления API

- [ ] Создан класс в папке `org.example.cs`
- [ ] Добавлены public методы для вызова из скриптов
- [ ] Зарегистрирован объект в `Main.kt` через `addGlobalObject()`
- [ ] Или зарегистрирована функция через `addGlobalFunction()`
- [ ] Типы параметров поддерживаются (Int, String, Boolean)
- [ ] Не используются null-able типы
- [ ] Написан пример использования
- [ ] Документация обновлена в README

---

## 🐛 Отладка собственных API

Если метод не вызывается, проверьте:

1. **Имя метода** совпадает и в коде, и в скрипте
2. **Типы параметров** поддерживаются интерпретатором
3. **Объект зарегистрирован** в `Main.kt`
4. **Нет опечаток** в имени объекта

Пример ошибки:
```kotlin
// Main.kt
interpreter.addGlobalObject("myapi", MyAPI())

// script.st
function main() {
    myapi.method()  // ✓ Правильно
    // mapi.method()  // ✗ Ошибка: Undefined variable: mapi
}
```

---

## 📚 Расширенные техники

### Сохранение состояния между вызовами

```kotlin
class CounterAPI {
    private var count = 0
    
    fun increment() {
        count++
    }
    
    fun getCount(): Int {
        return count
    }
}
```

**Использование:**
```kotlin
function main() {
    counter.increment()
    counter.increment()
    counter.increment()
    world.send("Count: " + counter.getCount())  // 3
}
```

### Взаимодействие между объектами

```kotlin
class PlayerAPI {
    var inventory: List<String> = listOf()
}

class InventoryAPI {
    fun add(item: String) {
        // Управление инвентарём
    }
}
```

---



## 📝 Комментарии

Поддерживаются два типа комментариев:

```kotlin
// C-style комментарии
-- Двойной минус

function main() {
    // Это будет проигнорировано
    world.send("Hello")  // Инлайн комментарий
}
```

---

## 📋 Лицензия

**GPL v3** — See [LICENSE](LICENSE)

Это означает:
- ✅ **Используй** — свободное использование
- ✅ **Изучай** — доступ к исходному коду
- ✅ **Изменяй** — модифицируй под свои нужды
- ⚠️ **Распределяй только с исходным кодом** — если распространяешь, включи исходный код
- ⚠️ **Производные работы под GPL v3** — любые модификации должны быть на GPL v3

---

## 👨‍💻 Развитие проекта

Потенциальные улучшения:

- [ ] Поддержка массивов и объектов
- [ ] Встроенные функции (len, push, pop и т.д.)
- [ ] Обработка исключений (try/catch)
- [ ] Импорт других файлов
- [ ] Отладчик и REPL
- [ ] Оптимизация JIT компиляции

---

## 📞 Контакты

**StoryScript Interpreter** — язык программирования для создания интерактивных скриптов на Kotlin
