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
