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