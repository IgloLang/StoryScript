function main() {
    console.printCyan("=== Калькулятор ===")
    console.newLine()
    
    while (true) {
        console.printGreen("Введите два числа и операцию (например: 5 + 3):")
        var input1 = input("Первое число: ")
        console.newLine()
        
        if (input1 == "exit") {
            console.printYellow("До свидания!")
            break
        }
        
        var op = input("Операция (+, -, *, /, %): ")
        console.newLine()
        var input2 = input("Второе число: ")
        console.newLine()
        
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
        } else if (op == "%") {
            if (num2 == 0) {
                console.printRed("Ошибка: деление на ноль!")
                console.newLine()
                continue
            }
            result = num1 % num2
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