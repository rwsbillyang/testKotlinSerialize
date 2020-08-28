import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
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
        }
    }

    val box = Box5(data = Person5("Tom"))
    val str = format.encodeToString(box)
    println("Contextual: $str")//Contextual: {"code":"OK","msg":null,"data":{"name":"Tom"}}

    try {
        val str2 = Json.encodeToString(box)//fail
        println("no Contextual: $str2")
    } catch (e: Exception) {
        //e.printStackTrace()
        println("fail: ${e.message}")
    }

}