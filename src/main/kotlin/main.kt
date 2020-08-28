
fun main(args: Array<String>) {
    println("========In testListBox: test List box with inheritance=========")
    testListBox()

    println("\n========In testEncodeDefaults: test encodeDefaults=false =========")
    testEncodeDefaults()

    println("\n========In testGeneric: test box generic type: T without customize serializer=========")
    testGeneric()

    println("\n========In testGenericWithCustomizedSerializer: test box generic type: T with customize serializer=========")
    testGenericWithCustomizedSerializer()

    println("\n========In testBoxAnyWithContextual: test box any with  @Contextual=========")
    testBoxAnyWithContextual()

    println("\n========In testBoxAnyWithPolymorphic: test box any with  @Polymorphic=========")
    testBoxAnyWithPolymorphic()
}