import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.*

@Serializable
private data class Box6(
        var code: String = "OK",
        var msg: String? = null,
        @Polymorphic var data: Any? = null
)

@Serializable
private data class Person6(val name: String)

fun testBoxAnyWithPolymorphic() {
    val format = Json {
        serializersModule = SerializersModule {
            polymorphic(Any::class) {
                subclass(Person6::class) //works ok
                //subclass(String::class) //fail: Exception in thread "main" java.lang.IllegalArgumentException: Serializer for String of kind STRING cannot be serialized polymorphically with class discriminator.
                //subclass(Long::class) //fail: Exception in thread "main" java.lang.IllegalArgumentException: Serializer for Long of kind LONG cannot be serialized polymorphically with class discriminator.
            }
        }
    }
    val box = Box6(data = Person6("Tom"))

    println("Polymorphic: ${format.encodeToString(box)}") //Polymorphic: {"code":"OK","msg":null,"data":{"type":"Person6","name":"Tom"}}

    try {
        println("Polymorphic with default Json: ${Json.encodeToString(box)}")
    } catch (e: Exception) {
        //fail box polymorphic with default Json: Class 'Person6' is not registered for polymorphic serialization in the scope of 'Any'.Mark the base class as 'sealed' or register the serializer explicitly.
        println("1. fail box polymorphic with default Json: ${e.message}")
    }
    try {
        val box2 = Box6(data ="Test box String")
        println("Box String with Polymorphic: ${format.encodeToString(box2)}") //if comment subclass(...)
    //Exception in thread "main" kotlinx.serialization.SerializationException: Class 'String' is not registered for polymorphic serialization in the scope of 'Any'. Mark the base class as 'sealed' or register the serializer explicitly.
    } catch (e: Exception) {
        //fail box polymorphic with default Json: Class 'Person6' is not registered for polymorphic serialization in the scope of 'Any'.Mark the base class as 'sealed' or register the serializer explicitly.
        println("2.fail box polymorphic: ${e.message}")
    }

    try {
    val data: Long = 1L
    val box3 = Box5(data = data)
    println("Box primitive Long with Polymorphic: ${format.encodeToString(box3)}")//if comment subclass(...)
    //Exception in thread "main" kotlinx.serialization.SerializationException: Serializer for class 'Any' is not found. Mark the class as @Serializable or provide the serializer explicitly.
    } catch (e: Exception) {
        //fail box polymorphic with default Json: Class 'Person6' is not registered for polymorphic serialization in the scope of 'Any'.Mark the base class as 'sealed' or register the serializer explicitly.
        println("3.fail box polymorphic: ${e.message}")
    }
    }