function main() {
    var list = new List()
    list.add("test1")
    list.add("test2")
    list.add("test3")
    list.remove("test2")
    world.send(list)
}