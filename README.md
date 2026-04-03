# 🎭 StoryScript

**Язык программирования для создания интерактивных скриптов**

Интерпретатор StoryScript позволяет выполнять скрипты с полной поддержкой функций, переменных, управления потоком, встроенных классов и интеграции с Java объектами. Написан на **Kotlin** с использованием классического подхода: лексер → парсер → интерпретатор.

**Версия:** 1.2.0  
**Язык реализации:** Kotlin на JVM  
**Требования:** Java 11+

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
    console.printGreen("Success!")
}
```

Результат:
```
Hello, StoryScript!
Success!
```

---

## ✨ Ключевые возможности

✅ **Динамическая типизация** — переменные меняют тип во время выполнения  
✅ **Функции первого класса** — функции как параметры и возвращаемые значения  
✅ **8 встроенных классов** — List, Map, Set, Stack, Queue, StringBuilder, Counter, Pair  
✅ **Rich API** — Math, String, Random, Time, File, Console  
✅ **Полная интеграция с Java** — вызывайте Java методы прямо из скриптов  
✅ **Automatic Type Coercion** — автоматическая конвертация типов  
✅ **Замыкания** — функции с лексическим захватом переменных  
✅ **Цветной вывод** — красивое форматирование консоли  

---

## 📚 Встроенные классы (v1.2)

### 1️⃣ List — Динамический массив

```kotlin
function main() {
    var list = new List()
    list.add("apple")
    list.add("banana")
    list.add("cherry")
    
    world.send(str(list))          // [apple, banana, cherry]
    world.send(str(list.size()))   // 3
    
    list.remove("banana")
    world.send(str(list))          // [apple, cherry]
}
```

**Методы:** `add(item)`, `remove(item)`, `remove(index)`, `get(index)`, `size()`, `clear()`, `contains(item)`, `indexOf(item)`

---

### 2️⃣ Map — Словарь ключ-значение

```kotlin
function main() {
    var map = new Map()
    map.set("name", "Alice")
    map.set("age", "30")
    map.set("city", "Moscow")
    
    world.send(str(map))           // {name: Alice, age: 30, city: Moscow}
    world.send(str(map.get("name"))) // Alice
    world.send(str(map.size()))    // 3
    
    map.remove("age")
    world.send(str(map.has("age"))) // false
}
```

**Методы:** `set(key, value)`, `get(key)`, `remove(key)`, `has(key)`, `size()`, `clear()`, `keys()`

---

### 3️⃣ Set — Набор уникальных элементов

```kotlin
function main() {
    var set = new Set()
    set.add("red")
    set.add("green")
    set.add("blue")
    set.add("red")  // Дубликат будет проигнорирован
    
    world.send(str(set))           // {red, green, blue}
    world.send(str(set.size()))    // 3
    world.send(str(set.has("red"))) // true
    
    set.remove("green")
    world.send(str(set))           // {red, blue}
}
```

**Методы:** `add(item)`, `remove(item)`, `has(item)`, `size()`, `clear()`

---

### 4️⃣ Stack — Стек (LIFO - Last In First Out)

```kotlin
function main() {
    var stack = new Stack()
    stack.push("first")
    stack.push("second")
    stack.push("third")
    
    world.send(str(stack))         // [third, second, first]
    world.send(str(stack.peek()))  // third
    
    var popped = stack.pop()
    world.send(str(popped))        // third
    world.send(str(stack))         // [second, first]
}
```

**Методы:** `push(item)`, `pop()`, `peek()`, `size()`, `isEmpty()`, `clear()`

---

### 5️⃣ Queue — Очередь (FIFO - First In First Out)

```kotlin
function main() {
    var queue = new Queue()
    queue.enqueue("task1")
    queue.enqueue("task2")
    queue.enqueue("task3")
    
    world.send(str(queue))         // [task1, task2, task3]
    world.send(str(queue.front())) // task1
    
    var processed = queue.dequeue()
    world.send(str(processed))     // task1
    world.send(str(queue))         // [task2, task3]
}
```

**Методы:** `enqueue(item)`, `dequeue()`, `front()`, `size()`, `isEmpty()`, `clear()`

---

### 6️⃣ StringBuilder — Построитель строк

```kotlin
function main() {
    var builder = new StringBuilder()
    builder.append("Hello")
    builder.append(" ")
    builder.append("World")
    
    world.send(str(builder))       // Hello World
    world.send(str(builder.length())) // 11
    
    var reversed = builder.reverse()
    world.send(str(reversed))      // dlroW olleH
    
    var upper = builder.toUpperCase()
    world.send(str(upper))         // HELLO WORLD
}
```

**Методы:** `append(text)`, `insert(index, text)`, `delete(start, end)`, `reverse()`, `clear()`, `length()`, `toUpperCase()`, `toLowerCase()`

---

### 7️⃣ Counter — Счётчик

```kotlin
function main() {
    var counter = new Counter()
    counter.increment()
    counter.increment()
    counter.increment()
    
    world.send(str(counter))       // 3
    
    counter.add(5)
    world.send(str(counter))       // 8
    
    counter.subtract(2)
    world.send(str(counter))       // 6
    
    counter.reset()
    world.send(str(counter))       // 0
}
```

**Методы:** `increment()`, `decrement()`, `add(value)`, `subtract(value)`, `get()`, `set(value)`, `reset()`

---

### 8️⃣ Pair — Пара значений

```kotlin
function main() {
    var pair = new Pair("key", "value")
    
    world.send(str(pair))          // (key, value)
    world.send(str(pair.first))    // key
    world.send(str(pair.second))   // value
    
    pair.first = "newKey"
    pair.second = "newValue"
    world.send(str(pair))          // (newKey, newValue)
    
    pair.swap()
    world.send(str(pair))          // (newValue, newKey)
}
```

**Методы/Свойства:** `first`, `second`, `swap()`

---

## 🔤 Типы данных и Синтаксис

### Основные типы

| Тип | Описание | Примеры |
|-----|---------|---------|
| **Number** | Int, Float, Double | `42`, `3.14`, `-5` |
| **String** | Текст | `"Hello"`, `'Single'` |
| **Boolean** | Истина/ложь | `true`, `false` |
| **null** | Нулевое значение | `null` |
| **Function** | Функция | `function foo() {}` |
| **Object** | Java/Kotlin объект | `entity(10)`, `world` |

### Переменные

```kotlin
var name = "Alice"          // String
var age = 25                // Int
var height = 1.75           // Float
var active = true           // Boolean
var value = null            // null

// Переприсвоение
var x = 10
x = "text"                  // ✓ Типы меняются динамически
```

### Функции

```kotlin
// Простая функция
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

// Вложенные функции
function outer() {
    function inner() {
        world.send("Inner function")
    }
    inner()
}

// Вызов
greet("Bob")
var sum = add(5, 3)
sayHi()
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

#### switch/case/default
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

#### Тернарный оператор
```kotlin
var status = age >= 18 ? "Adult" : "Minor"
var grade = score >= 90 ? "A" : score >= 80 ? "B" : "C"
```

### Циклы

#### while
```kotlin
var i = 0
while (i < 5) {
    world.send(i)
    i++
}
```

#### for
```kotlin
for (var i = 0; i < 5; i++) {
    world.send(i)
}

for (var x = 10; x > 0; x--) {
    world.send(x)
}
```

#### break
```kotlin
while (true) {
    if (condition) {
        break
    }
}
```

### Операторы

#### Арифметические
```kotlin
a + b           // Сложение
a - b           // Вычитание
a * b           // Умножение
a / b           // Деление
a // b          // Целое деление
a % b           // Остаток
a ^ b           // Степень (2^3 = 8)
-a              // Унарный минус
+b              // Унарный плюс
```

#### Логические
```kotlin
x && y          // И (AND)
x || y          // ИЛИ (OR)
!x              // НЕ (NOT)
```

#### Сравнение
```kotlin
a == b          // Мягкое равенство (с конвертацией)
a != b          // Не равно
a === b         // Строгое равенство (без конвертации)
a !== b         // Строгое не равенство
a > b           // Больше
a < b           // Меньше
a >= b          // Больше или равно
a <= b          // Меньше или равно
```

#### Составные операторы присваивания
```kotlin
x += 5          // x = x + 5
x -= 3          // x = x - 3
x *= 2          // x = x * 2
x /= 4          // x = x / 4
x %= 2          // x = x % 2
x **= 3         // x = x ^ 3
```

#### Инкремент/Декремент
```kotlin
x++             // Постфиксный: вернёт старое значение
++x             // Префиксный: вернёт новое значение
x--             // Постфиксный
--x             // Префиксный
```

#### Строковая конкатенация
```kotlin
"Hello" + " " + "World"     // "Hello World"
"Count: " + 42              // "Count: 42"
"Value: " + 3.14            // "Value: 3.14"
```

---

## 🔌 Встроенные API

### WorldAPI (объект `world`)

Глобальный объект для вывода и управления временем.

```kotlin
world.send("Hello")                    // Вывод текста
world.send("Player", "Action")         // С префиксом
world.time(3) {                        // Задержка 3 секунды
    world.send("Finished!")
}
```

---

### EntityAPI

```kotlin
var e = entity(42)
e.send()                    // [entity] 42
var value = e.send_return() // 42
```

---

### MathAPI (объект `math`)

```kotlin
var sqrt = math.sqrt(16)               // 4.0
var power = math.pow(2, 3)             // 8.0
var max = math.max(10, 20)             // 20
var min = math.min(10, 20)             // 10
var sin = math.sin(0)                  // 0.0
var pi = math.pi()                     // 3.14159...
```

**Методы:** `abs()`, `sqrt()`, `pow()`, `min()`, `max()`, `sin()`, `cos()`, `tan()`, `log()`, `log10()`, `exp()`, `round()`, `floor()`, `ceil()`, `pi()`, `e()`

---

### StringAPI (объект `string`)

```kotlin
var len = string.length("hello")       // 5
var upper = string.toUpperCase("hello")  // "HELLO"
var lower = string.toLowerCase("HELLO")  // "hello"
var has = string.contains("hello", "lo") // true
var parts = string.split("a,b,c", ",")  // ["a", "b", "c"]
```

**Методы:** `length()`, `substring()`, `indexOf()`, `contains()`, `replace()`, `toUpperCase()`, `toLowerCase()`, `trim()`, `split()`, `startsWith()`, `endsWith()`, `repeat()`, `charAt()`

---

### RandomAPI (объект `random`)

```kotlin
var chance = random.random()           // 0.0 - 1.0
var num = random.nextInt(100)          // 0 - 99
var num = random.nextInt(1, 10)        // 1 - 9
var dbl = random.nextDouble(50.0)      // 0.0 - 50.0
var flip = random.nextBoolean()        // true или false
var roll = random.dice()               // 1 - 6
var hundred = random.random100()       // 1 - 100
```

---

### TimeAPI (объект `time`)

```kotlin
var now = time.now()                   // "2026-04-03T14:30:45.123"
var time_str = time.time()             // "14:30:45"
var date = time.date()                 // "2026-04-03"
var hour = time.hour()                 // 14
var minute = time.minute()             // 30
```

---

### FileAPI (объект `file`)

```kotlin
var content = file.readFile("data.txt")         // Чтение
file.writeFile("output.txt", "Hello")           // Запись
file.appendFile("log.txt", "Event\n")           // Добавить
var exists = file.exists("file.txt")            // Проверка
file.delete("file.txt")                         // Удалить
```

---

### ConsoleAPI (объект `console`)

```kotlin
console.printRed("Error")              // 🔴 Красный текст
console.printGreen("Success")          // 🟢 Зелёный текст
console.printBlue("Info")              // 🔵 Синий текст
console.printYellow("Warning")         // 🟡 Жёлтый текст
console.printMagenta("Special")        // 🟣 Пурпурный текст
console.printCyan("Highlight")         // 🔷 Голубой текст
console.clear()                        // Очистить экран
console.println("text")                // Вывод с новой строкой
console.newLine()                      // Просто новая строка
console.separator()                    // Разделитель --------
console.centerText("Title", 50)        // Центрированный текст
```

---

## 🎯 Встроенные функции

### Ввод данных

```kotlin
var name = input("Ваше имя: ")         // Ввод строки
var age = int(input("Возраст: "))      // Ввод и конвертация
var height = float(input("Рост (м): ")) // Float
```

### Конвертация типов

```kotlin
var i = int("42")                      // String → Int → 42
var i = int(3.14)                      // Float → Int → 3
var i = int(true)                      // Boolean → Int → 1

var f = float("3.14")                  // String → Float → 3.14
var f = float(42)                      // Int → Float → 42.0

var d = double("2.71828")              // String → Double → 2.71828

var s = str(42)                        // Int → String → "42"
var s = str(3.14)                      // Float → String → "3.14"
var s = str(true)                      // Boolean → String → "true"
```

### Проверка типов

```kotlin
var type = typeof(42)                  // "Int"
var type = typeof(3.14)                // "Float"
var type = typeof("text")              // "String"
var type = typeof(true)                // "Boolean"

var formatted = format(42)             // "42 [Int]"
var formatted = format("hello")        // "hello [String]"
```

---

## 📖 Примеры программ

### Пример 1: Демонстрация встроенных классов

```kotlin
function main() {
    console.printCyan("=== Встроенные классы ===")
    console.newLine()
    
    // List
    console.printGreen("1. List:")
    var list = new List()
    list.add("apple")
    list.add("banana")
    console.println(str(list))
    console.newLine()
    
    // Map
    console.printGreen("2. Map:")
    var map = new Map()
    map.set("name", "Alice")
    map.set("age", "30")
    console.println(str(map))
    console.newLine()
    
    // Stack
    console.printGreen("3. Stack:")
    var stack = new Stack()
    stack.push("first")
    stack.push("second")
    console.println(str(stack))
    console.newLine()
    
    // Counter
    console.printGreen("4. Counter:")
    var counter = new Counter()
    counter.increment()
    counter.increment()
    counter.add(3)
    console.println(str(counter))
    console.newLine()
}
```

### Пример 2: Калькулятор

```kotlin
function main() {
    console.printCyan("=== Калькулятор ===")
    console.newLine()
    
    while (true) {
        var input1 = input("Первое число (или 'exit'): ")
        
        if (input1 == "exit") {
            console.printYellow("До свидания!")
            break
        }
        
        var op = input("Операция (+, -, *, /): ")
        var input2 = input("Второе число: ")
        
        var num1 = float(input1)
        var num2 = float(input2)
        var result = 0
        
        if (op == "+") {
            result = num1 + num2
        } else if (op == "-") {
            result = num1 - num2
        } else if (op == "*") {
            result = num1 * num2
        } else if (op == "/") {
            if (num2 == 0) {
                console.printRed("Ошибка: деление на ноль!")
                console.newLine()
                continue
            }
            result = num1 / num2
        } else {
            console.printRed("Неизвестная операция!")
            console.newLine()
            continue
        }
        
        console.printBlue("Результат: ")
        console.printMagenta(str(result))
        console.newLine()
        console.newLine()
    }
}
```

### Пример 3: Работа со Stack

```kotlin
function main() {
    console.printGreen("=== Демонстрация Stack ===")
    console.newLine()
    
    var stack = new Stack()
    
    // Добавление элементов
    stack.push("one")
    stack.push("two")
    stack.push("three")
    console.println("Stack: " + str(stack))
    
    // Просмотр верхнего элемента
    console.println("Top: " + str(stack.peek()))
    
    // Извлечение элементов
    while (!stack.isEmpty()) {
        console.println("Pop: " + str(stack.pop()))
    }
    
    console.println("Empty: " + str(stack.isEmpty()))
}
```

### Пример 4: Использование Map

```kotlin
function main() {
    var users = new Map()
    
    users.set("user1", "Alice")
    users.set("user2", "Bob")
    users.set("user3", "Charlie")
    
    console.println("Total users: " + str(users.size()))
    
    if (users.has("user1")) {
        console.println("User found: " + str(users.get("user1")))
    }
    
    console.println("All users: " + str(users))
}
```

### Пример 5: Случайная игра

```kotlin
function main() {
    var secretNumber = random.nextInt(1, 101)
    var attempts = 0
    var guessed = false
    
    console.printCyan("=== Угадай число ===")
    console.println("Я загадал число от 1 до 100")
    console.newLine()
    
    while (!guessed) {
        var guess = int(input("Попытка " + (attempts + 1) + ": "))
        attempts++
        
        if (guess == secretNumber) {
            console.printGreen("Правильно! Угадал за " + attempts + " попыток!")
            guessed = true
        } else if (guess < secretNumber) {
            console.printYellow("Число больше")
        } else {
            console.printYellow("Число меньше")
        }
    }
}

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

## ✨ Новые возможности (v1.1)

### Составные операторы присваивания
Компактный синтаксис для операций типа `x = x + 5`:

```kotlin
x += 5      // Сложение и присваивание
x -= 3      // Вычитание и присваивание
x *= 2      // Умножение и присваивание
x /= 4      // Деление и присваивание
x %= 2      // Остаток и присваивание
x **= 3     // Степень и присваивание
```

### Тернарный оператор
Выбор между двумя значениями на основе условия:

```kotlin
var status = age >= 18 ? "Adult" : "Minor"
var grade = score >= 90 ? "A" : score >= 80 ? "B" : "C"
```

### Функции ввода и конвертации типов
Полная поддержка входных данных и преобразования между типами:

```kotlin
var name = input("Ваше имя: ")              // Ввод строки
var age = int(input("Ваш возраст: "))       // Ввод Int
var height = float(input("Рост (м): "))     // Ввод Float
var weight = double(input("Вес (кг): "))    // Ввод Double

// Конвертация между типами
var intVal = int(3.14)              // → 3
var floatVal = float("2.5")         // → 2.5
var strVal = str(42)                // → "42"

// Проверка типов
var type = typeof(intVal)           // → "Int"
var formatted = format(intVal)      // → "42 [Int]"
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

### MathAPI (объект `math`)

Математические функции для вычислений.

```kotlin
var sqrt = math.sqrt(16)       // 4.0
var power = math.pow(2, 3)     // 8.0
var max = math.max(10, 20)     // 20
var sin = math.sin(0)          // 0.0
var pi = math.pi()             // 3.14159...
```

**Методы:** `abs()`, `sqrt()`, `pow()`, `min()`, `max()`, `sin()`, `cos()`, `tan()`, `log()`, `log10()`, `exp()`, `round()`, `floor()`, `ceil()`, `pi()`, `e()`

### StringAPI (объект `string`)

Работа со строками, поиск, замена, преобразование.

```kotlin
var len = string.length("hello")       // 5
var upper = string.toUpperCase("hello")  // "HELLO"
var parts = string.split("a,b,c", ",")  // ["a", "b", "c"]
var has = string.contains("hello", "lo") // true
```

**Методы:** `length()`, `substring()`, `indexOf()`, `contains()`, `replace()`, `toUpperCase()`, `toLowerCase()`, `trim()`, `split()`, `startsWith()`, `endsWith()`, `repeat()`, `charAt()`, и другие

### RandomAPI (объект `random`)

Генерирование случайных чисел и выбор.

#### `random(): Double`
Возвращает случайное число от 0.0 до 1.0
```kotlin
var chance = random.random()    // например: 0.7234
```

#### `nextInt(max): Int`
Случайное целое число от 0 до max (не включая max)
```kotlin
var num = random.nextInt(100)   // число от 0 до 99
```

#### `nextInt(min, max): Int`
Случайное целое число в диапазоне [min, max)
```kotlin
var num = random.nextInt(1, 10) // число от 1 до 9
```

#### `nextDouble(max): Double`
Случайное число с плавающей точкой от 0.0 до max
```kotlin
var num = random.nextDouble(50.0)  // например: 23.456
```

#### `nextDouble(min, max): Double`
Случайное число с плавающей точкой в диапазоне [min, max)
```kotlin
var num = random.nextDouble(10.0, 20.0)  // число между 10.0 и 20.0
```

#### `nextBoolean(): Boolean`
Возвращает случайный Boolean (true или false)
```kotlin
var flip = random.nextBoolean()  // true или false
```

#### `random100(): Int`
Случайное число от 1 до 100
```kotlin
var num = random.random100()  // число от 1 до 100
```

#### `dice(): Int`
Имитирует бросок кубика (число от 1 до 6)
```kotlin
var roll = random.dice()  // 1, 2, 3, 4, 5 или 6
```

**Примеры использования:**
```kotlin
function main() {
    // Простые примеры
    var coinFlip = random.nextBoolean() ? "Орёл" : "Решка"
    world.send("Результат: " + coinFlip)
    
    // Игра в кубики
    var dice1 = random.dice()
    var dice2 = random.dice()
    world.send("Сумма кубиков: " + (dice1 + dice2))
    
    // Случайное число в диапазоне
    var secretNum = random.nextInt(1, 21)  // 1-20
    world.send("Угадайте число от 1 до 20: " + secretNum)
    
    // Вероятность события (50%)
    if (random.random() < 0.5) {
        world.send("Вероятностное событие произошло!")
    }
}
```

### TimeAPI (объект `time`)

Работа со временем и датой.

```kotlin
var now = time.now()          // "2026-04-03T14:30:45.123"
var current = time.time()     // "14:30:45"
var date = time.date()        // "2026-04-03"
var hour = time.hour()        // 14
var minute = time.minute()    // 30
```

**Методы:** `now()`, `time()`, `date()`, `hour()`, `minute()`, `second()`, `dayOfMonth()`, `month()`, `year()`, `dayOfWeek()`

### FileAPI (объект `file`)

Чтение и запись файлов.

```kotlin
var content = file.readFile("data.txt")    // Чтение файла
file.writeFile("output.txt", "Hello")      // Запись файла
file.appendFile("log.txt", "Event\n")      // Добавление в конец
var exists = file.exists("file.txt")       // Проверка наличия
```

**Методы:** `readFile()`, `writeFile()`, `appendFile()`, `readLines()`, `exists()`, `delete()`, `size()`, `isDirectory()`, `isFile()`, `listFiles()`, `mkdir()`

### ConsoleAPI (объект `console`)

Форматирование вывода в консоль с цветами и стилями.

```kotlin
console.printRed("Error message")     // Красный текст
console.printGreen("Success!")        // Зелёный текст
console.printBlue("Info")             // Синий текст
console.separator()                   // Разделитель
console.clear()                       // Очистка экрана
```

**Методы:** `printRed()`, `printGreen()`, `printBlue()`, `printYellow()`, `printMagenta()`, `printCyan()`, `clear()`, `println()`, `newLine()`, `separator()`, `centerText()`

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

### Пример 12: Использование MathAPI, StringAPI, RandomAPI

```kotlin
function main() {
    world.send("=== Демонстрация библиотечных API ===")
    
    // Математика
    world.send("")
    world.send("--- MathAPI ---")
    var sqrt = math.sqrt(16)
    var pow = math.pow(2, 10)
    var rounded = math.round(3.7)
    
    world.send("sqrt(16) = " + sqrt)
    world.send("2^10 = " + pow)
    world.send("round(3.7) = " + rounded)
    world.send("π = " + math.pi())
    
    // Строки
    world.send("")
    world.send("--- StringAPI ---")
    var text = "Hello StoryScript"
    var length = string.length(text)
    var upper = string.toUpperCase(text)
    var lower = string.toLowerCase(text)
    
    world.send("Text: " + text)
    world.send("Length: " + length)
    world.send("UpperCase: " + upper)
    world.send("LowerCase: " + lower)
    world.send("Contains 'Script': " + string.contains(text, "Script"))
    
    // Случайные числа
    world.send("")
    world.send("--- RandomAPI ---")
    var dice1 = random.dice()
    var dice2 = random.dice()
    var random100 = random.nextInt(100)
    
    world.send("Dice 1: " + dice1)
    world.send("Dice 2: " + dice2)
    world.send("Random (0-99): " + random100)
    world.send("Total: " + (dice1 + dice2))
    
    // Время
    world.send("")
    world.send("--- TimeAPI ---")
    var currentTime = time.time()
    var currentDate = time.date()
    var hour = time.hour()
    var minute = time.minute()
    
    world.send("Time: " + currentTime)
    world.send("Date: " + currentDate)
    world.send("Hour: " + hour + ", Minute: " + minute)
}
```

**Результат:**
```
=== Демонстрация библиотечных API ===

--- MathAPI ---
sqrt(16) = 4.0
2^10 = 1024.0
round(3.7) = 4.0
π = 3.141592653589793

--- StringAPI ---
Text: Hello StoryScript
Length: 16
UpperCase: HELLO STORYSCRIPT
LowerCase: hello storyscript
Contains 'Script': true

--- RandomAPI ---
Dice 1: 4
Dice 2: 2
Random (0-99): 47
Total: 6

--- TimeAPI ---
Time: 14:30:45
Date: 2026-04-03
Hour: 14, Minute: 30
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

## � Шпаргалка по новому синтаксису

### Составные операторы

| Оператор | Эквивалент | Пример |
|----------|------------|--------|
| `+=` | `x = x + y` | `count += 1` |
| `-=` | `x = x - y` | `health -= 10` |
| `*=` | `x = x * y` | `value *= 2` |
| `/=` | `x = x / y` | `result /= 2` |
| `%=` | `x = x % y` | `mod %= 3` |
| `**=` | `x = x ** y` | `power **= 2` |

### Функции ввода и типизации

| Функция | Описание | Пример |
|---------|---------|--------|
| `input(prompt)` | Ввод строки с сообщением | `var name = input("Имя: ")` |
| `int(value)` | Конвертирует в целое число | `var age = int(input(...))` |
| `float(value)` | Конвертирует в Float | `var height = float("1.75")` |
| `double(value)` | Конвертирует в Double | `var pi = double("3.14159")` |
| `str(value)` | Конвертирует в строку | `var text = str(42)` |
| `typeof(value)` | Возвращает тип значения | `world.send(typeof(x))` |
| `format(value)` | Форматирует со типом | `world.send(format(x))` |

### Тернарный оператор

```
condition ? true_value : false_value
```

**Примеры:**
```kotlin
var status = age >= 18 ? "Adult" : "Minor"
var grade = score >= 90 ? "A" : score >= 80 ? "B" : "C"
```

---

## �🛑 Обработка ошибок

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

## � Полезные советы и типичные ошибки

### ✅ Что работает

```kotlin
// Все типы данных поддерживаются
var x = 42              // Int
var y = 3.14           // Float
var z = "text"         // String

// Составные операторы
x += 10                // Работает!

// Тернарный оператор
var status = x > 50 ? "High" : "Low"

// Ввод данных
var name = input("Имя: ")
var age = int(input("Возраст: "))

// Проверка типов
world.send(typeof(x))  // "Int"
world.send(format(y))  // "3.14 [Float]"
```

### ❌ Типичные ошибки и как их избежать

| Ошибка | Проблема | Решение |
|--------|----------|--------|
| `var x; x = 5` | Нельзя объявить без инициализации | Используйте `var x = null` или `var x = 0` |
| `int("abc")` | Конвертация невалидной строки | Вернёт `0`, используйте проверку |
| `float("3,14")` | Использована запятая вместо точки | Используйте точку: `float("3.14")` |
| `x / 0` | Деление на ноль | Проверяйте делитель перед операцией |
| `function test() {}; test` | Забыли скобки при вызове | Используйте `test()` |
| `world.send()` | Функция требует аргумент | Добавьте сообщение: `world.send("text")` |

### 🎯 Лучшие практики

1. **Всегда используйте типизацию при вводе:**
   ```kotlin
   var age = int(input("Age: "))     // ✓ Правильно
   var age = input("Age: ")          // ✗ Неправильно - строка
   ```

2. **Проверяйте типы перед использованием:**
   ```kotlin
   world.send(typeof(x))  // Узнать, что имеем
   world.send(format(x))  // Красивый вывод
   ```

3. **Используйте составные операторы для лаконичности:**
   ```kotlin
   count += 1             // ✓ Лучше
   count = count + 1      // ✗ Многословно
   ```

4. **Используйте тернарный оператор для простых условий:**
   ```kotlin
   var result = x > 0 ? "Positive" : "Negative"  // ✓
   ```

5. **Группируйте логику в функции:**
   ```kotlin
   function calculateTotal(a, b, c) {
       var sum = a + b + c
       return sum
   }
   
   function main() {
       var total = calculateTotal(10, 20, 30)
       world.send("Total: " + total)
   }
   ```

---

## �👨‍💻 Развитие проекта

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
