package testJsonTransformingSerializer

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*


fun main(){
    val filter1 = Target().apply{
        type = FilterType.Tag
        filter = TagFilter(false, 2)
    }
    val filter2 = Target().apply {
        type = FilterType.List
        list = listOf("openId1, openId2")
    }
    val filter3 = Target().apply{
        type = FilterType.OpenId
        openId = "theUserOpenID"
    }
    val filter4 = Target().apply {
        type = FilterType.WxName
        wxName = "the wx name"
    }

    //{"msgType":"text","content":"msg1","target":{"filter":{"is_to_all":false,"tagId":2}}}
    val textMsg1 = TextMsg("msg1",filter1)

    println(Json.encodeToString(textMsg1))
}
/**
 * 不同的值类型
 * */
@Serializable
enum class FilterType{
    Tag, List, OpenId, WxName
}

@Serializable
class TagFilter(
        @SerialName("is_to_all")
        val isToAll: Boolean = false,

        @SerialName("tagId")
        val tagId: Int? = null
)

@Serializable
class Target{
    var type: FilterType  = FilterType.Tag

    var filter: TagFilter? = null
    var list: List<String>? = null
    var openId: String? = null
    var wxName: String? = null
}


@Serializable
open  class Msg(val msgType: String)
//{
//    var type: FilterType  = FilterType.Tag
//    var filter: TagFilter? = null
//    var list: List<String>? = null
//    var openId: String? = null
//    var wxName: String? = null
//}

@Serializable
class TextMsg(
        val content: String,
        @Serializable(with = TargetSerializer::class)
        val target: Target): Msg("text")

@Serializable
class ImgMsg(val mediaId: String,  val target: Target): Msg("image")

/**
 * 去掉所有空值字段，以及key为type字段
 * */
object TargetSerializer : JsonTransformingSerializer<Target>(Target.serializer()) {
    override fun transformSerialize(element: JsonElement): JsonElement
            = JsonObject(element.jsonObject.filterNot { it.value is JsonNull  || it.key == "type"})
}


fun promte(element: JsonElement): JsonElement{

    val map: MutableMap<String, JsonElement> = element.jsonObject.toMutableMap()
    map.forEach {
        val target: JsonObject = it.value.jsonObject
        when(target["filterType"]?.jsonPrimitive?.content){
            FilterType.Tag.name -> map["filter"] = target["filter"]!!
            FilterType.List.name -> map["touser"] = target["list"]!!
            FilterType.OpenId.name -> map["touser"] = target["openId"]!!
            FilterType.WxName.name -> map["towxname"] = target["wxName"]!!
        }
    }
    return JsonObject(map.toMap())
}