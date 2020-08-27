import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Student(val name: String)

// put base class external lib
//@Serializable
//data class ListBox1(val list: List<Student>?, val total: Int = 0) :  com.github.rwsbillyang.apiJson.Box()

@Serializable
data class ListBox2(val list: List<Student>?, val total: Int = 0) : Box()

fun test1()
{
    val json = Json{}

    val list = listOf(Student("Tom"), Student("Joe"))
    val size = list.size

//    val box1 = ListBox1(list, size)
//    println("box1: code=${box1.code},msg=${box1.msg},total=${box1.total}")
//    val str1 = json.encodeToString(ListBox1.serializer(), box1) // inheritance from external library
//    println("box1 json: $str1")
//    println()

    val box2 = ListBox2(list, size)
    println("box2: code=${box2.code},msg=${box2.msg},total=${box2.total}")
    val str2 = json.encodeToString(ListBox2.serializer(), box2)// inheritance from same project
    println("box2 json: $str2")

}