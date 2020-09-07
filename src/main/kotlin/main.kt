
fun main(args: Array<String>) {
    println("========1 In testListBox: test List box with inheritance=========")
    testListBox()

    println("\n========2 In testEncodeDefaults: test encodeDefaults=false =========")
    testEncodeDefaults()

    println("\n========3 In testGeneric: test box generic type: T without customize serializer=========")
    testGeneric()

    println("\n========4 In testCustomSerializer: customize serializer=========")
    testCustomSerializer()

    println("\n========5 In testCustomSerializerAndInheritance: customize serializer and inheritance=========")
    testCustomSerializerAndInheritance()

    println("\n========6 In testGenericWithCustomizedSerializer: test box generic type: T with customize serializer=========")
    testGenericWithCustomizedSerializer()

    println("\n========7 In testBoxAnyWithContextual: test box any with  @Contextual=========")
    testBoxAnyWithContextual()

    println("\n========8 In testBoxAnyWithPolymorphic: test box any with  @Polymorphic=========")
    testBoxAnyWithPolymorphic()


}