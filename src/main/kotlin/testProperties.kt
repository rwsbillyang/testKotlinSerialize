package testProperties

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.properties.Properties // todo: remove when no longer needed
import kotlinx.serialization.properties.*
import java.net.URLDecoder

@Serializable
data class Project(val name: String, val owner: User)

@Serializable
data class User(val name: String)

//https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/formats.md#properties-experimental
fun main() {
    val data = Project("kotlinx.serialization", User("kotlin"))
    val map = Properties.encodeToMap(data)
    map.forEach { (k, v) -> println("$k = $v") }

   // val str = Json.encodeToString(map)
   // println(str) //Exception in thread "main" kotlinx.serialization.SerializationException: Serializer for class 'Any' is not found.

    //val jsonElement = Json.parseToJsonElement(str)


    val data2 = Properties.decodeFromMap<Project>(map)
    println("data2=$data2")

    //{current: 1,pageSize: 20,sorter: {"callNo":"ascend"},filter: {}
    val encodeURIComponent = "%7Bcurrent%3A%201%2CpageSize%3A%2020%2Csorter%3A%20%7B%22callNo%22%3A%22ascend%22%7D%2Cfilter%3A%20%7B%7D"
    val uri = URLDecoder.decode(encodeURIComponent, "UTF-8")
    println("uri: $uri")//uri: {current: 1,pageSize: 20,sorter: {"callNo":"ascend"},filter: {}
}