import ru.scripter.init.MainInit

fun main(args: Array<String>) {
    val scriptPath = if (args.isNotEmpty()) {
        args[0]
    } else {
        "script.st"  // Default script file
    }
    
    MainInit(scriptPath).run()
}