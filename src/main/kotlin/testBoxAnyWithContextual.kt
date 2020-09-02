//@file:UseContextualSerialization(Person5::class)
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.*

@Serializable
data class Box5(
        var code: String = "OK",
        var msg: String? = null,
        @Contextual var data: Any? = null
)

@Serializable
data class Person5(val name: String)

fun testBoxAnyWithContextual() {
    val format = Json {
        serializersModule = SerializersModule {
            contextual(Person5.serializer())
            contextual(String.serializer())//Need add this line, "Any" can be String
            contextual(Long.serializer())//Need add this line, "Any" can be Long
        }
    }

    val box = Box5(data = Person5("Tom"))
    println("Contextual: ${format.encodeToString(box)}") // ok if add: contextual(Person5.serializer())
    //Contextual: {"code":"OK","msg":null,"data":{"name":"Tom"}}

    try {
        println("Contextual with default Json: : ${Json.encodeToString(box)}")
    } catch (e: Exception) {
        //fail with default Json: Serializer for class 'Any' is not found.Mark the class as @Serializable or provide the serializer explicitly.
        println("fail with default Json: ${e.message}")
    }

    val box2 = Box5(data ="Test box String")
    println("Box primitive String with Contextual: ${format.encodeToString(box2)}")//ok if add: contextual(String.serializer())
    //Box primitive String with Contextual: {"code":"OK","msg":null,"data":"Test box String"}


    val data: Long = 1L
    val box3 = Box5(data = data)
    println("Box primitive Long with Contextual: ${format.encodeToString(box3)}")//ok if add: contextual(Long.serializer())
    //Box primitive Long with Contextual: {"code":"OK","msg":null,"data":1}
}