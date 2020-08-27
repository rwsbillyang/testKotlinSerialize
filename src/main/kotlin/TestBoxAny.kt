import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Person(val name: String)

fun test3(){
    val json = Json{}
    val str = json.encodeToString(DataBox.serializer(),DataBox(data = Person("Tom")))
    println(str)
}