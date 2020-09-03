import kotlinx.serialization.Serializable
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Box3<T>(var code: String = "OK", var msg: String? = null, val data: T? = null)
@Serializable
data class Person3(val name: String)

/**
 * Test box generic
 *
 * https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/basic-serialization.md#generic-classes
 * */
fun testGeneric(){
    // {"code":"OK","msg":null,"data":{"name":"Tom"}}
    println(Json.encodeToString(Box3(data = Person3("Tom"))) )

    //{"code":"OK","msg":null,"data":[{"name":"Tom"},{"name":"Tom2"}]}
    println(Json.encodeToString(Box3(data = listOf(Person3("Tom"),Person3("Tom2")))))

    //{"code":"OK","msg":null,"data":"box primitive String"}
    println(Json.encodeToString(Box3(data = "box primitive String")))

    //{"code":"OK","msg":null,"data":1.23}
    println(Json.encodeToString(Box3(data = 1.23F)))
}