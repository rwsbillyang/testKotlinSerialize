import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.Json


/**
 * Custom serializers for a generic type
 * https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serializers.md#custom-serializers-for-a-generic-type
 * */

@Serializable(with = BoxSerializer::class)
data class Box4<T>(var code: String = "OK",
                   var msg: String? = null, val data: T? = null)
@Serializable
data class Person4(val name: String)
/**
 * 测试包含泛型的自定义serializer
 * */
fun testGenericWithCustomizedSerializer(){
    val format = Json{}

    //指定和不指定serializer没有区别： 此处指定了
    val str = format.encodeToString(Box4.serializer(Person4.serializer()),Box4(data = Person4("Tom")))
    println(str)//{"code4":"OK","msg4":null,"data4":{"name":"Tom"}}

    //指定和不指定serializer没有区别： 此处指定了
    val str2 = format.encodeToString(Box4.serializer(ListSerializer(Person4.serializer())),Box4(data = listOf(Person4("Tom"),Person4("Tom2"))))
    println(str2)//{"code4":"OK","msg4":null,"data4":[{"name":"Tom"},{"name":"Tom2"}]}

    //指定和不指定serializer没有区别： 此处没有指定
    val str3 = Json.encodeToString(Box4(data = Person4("Tom3")))
    println(str3)//{"code4":"OK","msg4":null,"data4":{"name":"Tom3"}}
}
/**
 * 注意key字段被修改: code4,msg4,data4
 * */
class BoxSerializer<T>(private val dataSerializer: KSerializer<T?>) : KSerializer<Box4<T>> {
    //override val descriptor: SerialDescriptor = dataSerializer.descriptor
//    override fun serialize(encoder: Encoder, value: Box2<T>) = dataSerializer.serialize(encoder, value.data)
//    override fun deserialize(decoder: Decoder) = Box2(dataSerializer.deserialize(decoder))
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Box") {
            element<String>("code4")
            element<String?>("msg4")
            //element("nullableString", serialDescriptor<String>().nullable)
            element("data4",dataSerializer.descriptor)
        }
    @ExperimentalSerializationApi
    override fun serialize(encoder: Encoder, value: Box4<T>) =
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.code)
            encodeNullableSerializableElement(descriptor, 1,String.serializer(), value.msg)
            encodeNullableSerializableElement(descriptor, 2, dataSerializer, value.data)
        }
    @ExperimentalSerializationApi
    override fun deserialize(decoder: Decoder): Box4<T> =
        decoder.decodeStructure(descriptor) {
            var code: String = "OK"
            var msg: String? = null
            var data: T? = null
            while(true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> code = decodeStringElement(descriptor, 0)
                    1 -> msg = decodeNullableSerializableElement(descriptor, 1, String.serializer().nullable, null)
                    2 -> data = decodeNullableSerializableElement(descriptor, 2, dataSerializer, null) //DeserializationStrategy
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }

            Box4(code, msg, data)
        }
}

