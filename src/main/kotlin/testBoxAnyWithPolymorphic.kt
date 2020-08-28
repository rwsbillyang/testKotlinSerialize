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
                subclass(Person6::class) //注册运行时子类
            }
        }
    }
    val box = Box6(data = Person6("Tom"))

    val str = format.encodeToString(box)
    println("Polymorphic: $str") //Polymorphic: {"code":"OK","msg":null,"data":{"type":"Person6","name":"Tom"}}

    try {
        val str2 = Json.encodeToString(box) // fail
        println("no Polymorphic: $str2")
    } catch (e: Exception) {
        //e.printStackTrace()
        println("fail: ${e.message}")
    }

}