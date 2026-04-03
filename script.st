function main() {
    // Примеры составных операторов присваивания
    var x = 10
    world.send("x = " + x)
    
    x += 5
    world.send("x += 5 → " + x)  // 15
    
    x -= 3
    world.send("x -= 3 → " + x)  // 12
    
    x *= 2
    world.send("x *= 2 → " + x)  // 24
    
    x /= 4
    world.send("x /= 4 → " + x)  // 6
    
    x %= 2
    world.send("x %= 2 → " + x)  // 0
    
    // Примеры тернарного оператора
    world.send("")
    var age = 25
    var status = age >= 18 ? "Adult" : "Minor"
    world.send("Age: " + age + " → " + status)
    
    age = 15
    status = age >= 18 ? "Adult" : "Minor"
    world.send("Age: " + age + " → " + status)
    
    // Вложенные тернарные операторы
    var score = 85
    var grade = score >= 90 ? "A" : score >= 80 ? "B" : score >= 70 ? "C" : "F"
    world.send("Score: " + score + " → Grade: " + grade)
    
    score = 75
    grade = score >= 90 ? "A" : score >= 80 ? "B" : score >= 70 ? "C" : "F"
    world.send("Score: " + score + " → Grade: " + grade)
}