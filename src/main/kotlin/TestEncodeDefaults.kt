import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class Project2(val name: String, val renamedTo: String? = null, val level: Int = 100, val title: String? = null)

fun testEncodeDefaults(){
    val json = Json{ encodeDefaults = false }
    val data = Project2("kotlinx.serialization", title="kotlin")

    //{"name":"kotlinx.serialization","title":"kotlin"}, removed the key: level
    println(json.encodeToString(Project2.serializer(), data))

    //{"name":"kotlinx.serialization1.0"}
    println(json.encodeToString(Project2.serializer(), Project2("kotlinx.serialization1.0",null, level=100, null)))
}


