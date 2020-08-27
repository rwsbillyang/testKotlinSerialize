import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.Json


/**
 * Custom serializers for a generic type
 * https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serializers.md#custom-serializers-for-a-generic-type
 * */

@Serializable(with = BoxSerializer::class)
data class Box2<T>(var code: String = "OK",
                   var msg: String? = null, val data: T? = null)


class BoxSerializer<T>(private val dataSerializer: KSerializer<T?>) : KSerializer<Box2<T>> {
    //override val descriptor: SerialDescriptor = dataSerializer.descriptor
//    override fun serialize(encoder: Encoder, value: Box2<T>) = dataSerializer.serialize(encoder, value.data)
//    override fun deserialize(decoder: Decoder) = Box2(dataSerializer.deserialize(decoder))
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Box") {
            element<String>("code")
            element<String?>("msg")
            //element("nullableString", serialDescriptor<String>().nullable)
            element("data",dataSerializer.descriptor)
        }
    @ExperimentalSerializationApi
    override fun serialize(encoder: Encoder, value: Box2<T>) =
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.code)
            encodeNullableSerializableElement(descriptor, 1,String.serializer(), value.msg)
            encodeNullableSerializableElement(descriptor, 2, dataSerializer, value.data)
        }
    @ExperimentalSerializationApi
    override fun deserialize(decoder: Decoder): Box2<T> =
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

            Box2(code, msg, data)
        }
}

fun test4(){
    val json = Json{}
    val str = json.encodeToString(Box2.serializer(Person.serializer()),Box2(data = Person("Tom")))
    println(str)

    val str2 = json.encodeToString(Box2.serializer(ListSerializer(Person.serializer())),Box2(data = listOf(Person("Tom"),Person("Tom2"))))
    println(str2)
}