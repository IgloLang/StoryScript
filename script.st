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