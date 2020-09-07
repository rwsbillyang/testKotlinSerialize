import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json

fun testCustomSerializer(){
    val filter1 = Filter().apply{
        type = FilterType.Tag
        filter = UserFilter(false, 2)
    }
    val filter2 = Filter().apply {
        type = FilterType.OpenIds
        openIds = listOf("openId1, openId2")
    }
    val filter3 = Filter().apply{
        type = FilterType.OpenId
        openId = "theUserOpenID"
    }
    val filter4 = Filter().apply {
        type = FilterType.WxName
        wxName = "the wx name"
    }

    println(Json.encodeToString(filter1)) //{"filter":{"is_to_all":false,"tagId":2}}
    println(Json.encodeToString(filter2)) //{"touser":["openId1, openId2"]}
    println(Json.encodeToString(filter3)) //{"touser":"theUserOpenID"}
    println(Json.encodeToString(filter4)) //{"towxname":"the wx name"}

    /*if use FilterSerializer2 then:
    {"is_to_all":false,"tagId":2}
    ["openId1, openId2"]
    "theUserOpenID"
    "the wx name"
    */

    val textMsg1 = TextMsg("msg1").apply {
        type = FilterType.Tag
        filter = UserFilter(false, 2)
    }
    val textMsg2 = TextMsg("msg2").apply {
        type = FilterType.OpenIds
        openIds = listOf("openId1, openId2")
    }
    val textMsg3 = TextMsg("msg3").apply {
        type = FilterType.OpenId
        openId = "theUserOpenID"
    }
    val textMsg4 = TextMsg("msg4").apply {
        type = FilterType.WxName
        wxName = "the wx name"
    }

    println(Json.encodeToString(textMsg1)) //
    println(Json.encodeToString(textMsg2)) //
    println(Json.encodeToString(textMsg3)) //
    println(Json.encodeToString(textMsg4))
}

/**
 * 不同的值类型
 * */
@Serializable
enum class FilterType{
    Tag, OpenIds, OpenId, WxName
}

@Serializable
class UserFilter(
        @SerialName("is_to_all")
        val isToAll: Boolean = false,

        @SerialName("tagId")
        val tagId: Int? = null
)

/**
 * 将根据不同的type类型的值，分别序列化不同的子段值
 * */
@Serializable(with = FilterSerializer::class)
//@Serializable
open class Filter{
    var type: FilterType  = FilterType.Tag
    var filter: UserFilter? = null
    @SerialName("touser")
    var openIds: List<String>? = null
    @SerialName("touser2")
    var openId: String? = null
    @SerialName("towxname")
    var wxName: String? = null
}


@Serializable
class TextMsg(
        val text: String,
        @SerialName("msgtype")
        val msgType: String = "text"
): Filter()

object FilterSerializer : KSerializer<Filter> {
    //override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Filter", PrimitiveKind.STRING)
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Filter") {
            element<Int?>("filter", isOptional = true)
            element<List<String>?>("touser", isOptional = true)
            element<String?>("touser2", isOptional = true)
            element<String?>("towxname", isOptional = true)
        }


    override fun serialize(encoder: Encoder, value: Filter) =
            encoder.encodeStructure(descriptor) {
                when(value.type){
                    FilterType.Tag -> encodeSerializableElement(descriptor,0, UserFilter.serializer(), value.filter!!)
                    FilterType.OpenIds -> encodeSerializableElement(descriptor,1, ListSerializer(String.serializer()), value.openIds!!)
                    FilterType.OpenId -> encodeStringElement(descriptor, 1, value.openId!!)//此处index为1，key将是touser，若为2则使用touser2
                    FilterType.WxName -> encodeStringElement(descriptor, 3, value.wxName!!)
                }
            }

    override fun deserialize(decoder: Decoder): Filter {
//        val type = decoder.decodeInt()
//        return Filter(FilterType.Tag)
        TODO("Not implement")
    }
}

/**
 * if use FilterSerializer2 then:
    {"is_to_all":false,"tagId":2}
    ["openId1, openId2"]
    "theUserOpenID"
    "the wx name"
*/
object FilterSerializer2 : KSerializer<Filter> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Filter", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Filter) =
            when(value.type){
                FilterType.Tag -> UserFilter.serializer().serialize(encoder,value.filter!!)
                FilterType.OpenIds ->  ListSerializer(String.serializer()).serialize(encoder, value.openIds!!)
                FilterType.OpenId -> String.serializer().serialize(encoder, value.openId!!)
                FilterType.WxName -> String.serializer().serialize(encoder,  value.wxName!!)
            }

    override fun deserialize(decoder: Decoder): Filter {
        TODO("Not implement")
    }
}


