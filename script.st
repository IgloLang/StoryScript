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