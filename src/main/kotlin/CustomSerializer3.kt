package customSerializer3

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.ClassSerialDescriptorBuilder
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonEncoder


/**
 * 不同的值类型
 * */
@Serializable
enum class FilterType {
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
@Serializable
//@Serializable
open class Filter {
    var type: FilterType = FilterType.Tag
    var filter: UserFilter? = null

    @SerialName("touser")
    var openIds: List<String>? = null

    @SerialName("touser2")
    var openId: String? = null

    @SerialName("towxname")
    var wxName: String? = null
}


fun main() {
    val filter1 = Filter().apply {
        type = FilterType.Tag
        filter = UserFilter(false, 2)
    }
    val filter2 = Filter().apply {
        type = FilterType.OpenIds
        openIds = listOf("openId1, openId2")
    }
    val filter3 = Filter().apply {
        type = FilterType.OpenId
        openId = "theUserOpenID"
    }
    val filter4 = Filter().apply {
        type = FilterType.WxName
        wxName = "the wx name"
    }

    val textMsg1 = TextMsg("msg1",  filter1)
    val textMsg2 = TextMsg("msg1",  filter2)
    val textMsg3 = TextMsg("msg1", filter3)
    val textMsg4 = TextMsg("msg1",  filter4)

    println(Json.encodeToString(textMsg1)) //{"content":"msg1","msgtype":"text","filter":{"is_to_all":false,"tagId":2}}
    println(Json.encodeToString(textMsg2)) // {"content":"msg1","msgtype":"text","touser":["openId1, openId2"]}
    println(Json.encodeToString(textMsg3)) //{"content":"msg1","msgtype":"text","touser":"theUserOpenID"}
    println(Json.encodeToString(textMsg4)) //{"content":"msg1","msgtype":"text","towxname":"the wx name"}
}


interface IMsg{
    val target: Filter
    val msgType: String
}

@Serializable(with = TextMsgSerializer::class)
class TextMsg(
        val content: String,
        override val target: Filter,
        override val msgType: String = "text"
): IMsg



fun ClassSerialDescriptorBuilder.addElement(){
    element<String>("msgtype", isOptional = true)
    element<String>("filter", isOptional = true)
    element<String>("touser", isOptional = true)
    element<String>("towxname", isOptional = true)
}

fun CompositeEncoder.serializeTarget(value: IMsg){
    encodeStringElement(TextMsgSerializer.descriptor, 1, value.msgType)
    when (value.target.type) {
        FilterType.Tag -> encodeSerializableElement(TextMsgSerializer.descriptor, 2, UserFilter.serializer(), value.target.filter!!)
        FilterType.OpenIds -> encodeSerializableElement(TextMsgSerializer.descriptor, 3, ListSerializer(String.serializer()), value.target.openIds!!)
        FilterType.OpenId -> encodeStringElement(TextMsgSerializer.descriptor, 3, value.target.openId!!)
        FilterType.WxName -> encodeStringElement(TextMsgSerializer.descriptor, 4, value.target.wxName!!)
    }
}



object TextMsgSerializer : KSerializer<TextMsg> {
    override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor("TextMsg") {
                element<Int?>("content", isOptional = true)
                addElement()
            }


    override fun serialize(encoder: Encoder, value: TextMsg) =
            encoder.encodeStructure(descriptor) {
                encodeStringElement(descriptor, 0, value.content)
                serializeTarget(value)

                // Encoder -> JsonEncoder
                // require(encoder is JsonEncoder) // This class can be encoded only by Json
                // val element = buildJsonObject { put("target", encoder.json.encodeToJsonElement(Filter.serializer(), value.filter)) }
                //  encoder.encodeJsonElement(encoder.json.encodeToJsonElement(Filter.serializer(), value.filter))

            }

    override fun deserialize(decoder: Decoder): TextMsg {
        TODO("Not implement")
    }
}




//object MsgSerializer : KSerializer<Msg> {
//    override val descriptor: SerialDescriptor =
//            buildClassSerialDescriptor("Msg") {
//                element<String>("msgtype", isOptional = true)
//                element<String>("filter", isOptional = true)
//                element<String>("touser", isOptional = true)
//                element<String>("towxname", isOptional = true)
//            }
//
//    override fun serialize(encoder: Encoder, value: Msg) =
//            encoder.encodeStructure(descriptor) {
//                encodeStringElement(descriptor, 0, value.msgType)
//                when (value.target.type) {
//                    FilterType.Tag -> encodeSerializableElement(descriptor, 1, UserFilter.serializer(), value.target.filter!!)
//                    FilterType.OpenIds -> encodeSerializableElement(descriptor, 2, ListSerializer(String.serializer()), value.target.openIds!!)
//                    FilterType.OpenId -> encodeStringElement(descriptor, 2, value.target.openId!!)
//                    FilterType.WxName -> encodeStringElement(descriptor, 3, value.target.wxName!!)
//                }
//            }
//
//    override fun deserialize(decoder: Decoder): Msg {
//        TODO("Not implement")
//    }
//}