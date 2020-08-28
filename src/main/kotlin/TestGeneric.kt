import kotlinx.serialization.Serializable
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Box3<T>(var code: String = "OK", var msg: String? = null, val data: T? = null)
@Serializable
data class Person3(val name: String)

/**
 * /**
 * 测试泛型
 * */
 * */
//https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/basic-serialization.md#generic-classes
fun testGeneric(){
    // {"code":"OK","msg":null,"data":{"name":"Tom"}}
    println(Json.encodeToString(Box3(data = Person3("Tom"))) )

    //{"code":"OK","msg":null,"data":[{"name":"Tom"},{"name":"Tom2"}]}
    val str2 = Json.encodeToString(Box3(data = listOf(Person3("Tom"),Person3("Tom2"))))
    println(str2)
}