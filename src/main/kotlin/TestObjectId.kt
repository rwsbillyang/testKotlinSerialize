
@file:UseContextualSerialization(ObjectId::class)

package testObjectId


import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.bson.types.ObjectId
import java.util.*

/**
 * login后返回的信息
 * */
@Serializable
data class AuthBean(
        val uId: ObjectId,
        val level: Int,
        val role: List<String>?,
        val token: String
)
@Serializable
data class DataBox<T>(
        val code: String,
        val msg: String? = null,
        val data: T? = null)
{
    companion object{
        fun <T> ok(data: T?) = DataBox("OK", data = data)
        fun <T> ko(msg: String) = DataBox<T>("KO", msg)
        fun <T> needLogin(msg: String) = DataBox<T>("NeedLogin", msg)
    }
}

val apiJson = Json {
    encodeDefaults = false
    ignoreUnknownKeys = true
    //isLenient = true
    allowSpecialFloatingPointValues = true
    useArrayPolymorphism = false
    serializersModule = SerializersModule{
        //ObjectId::class to ObjectIdBase64Serializer //reports exception
        contextual(ObjectIdBase64Serializer)
        //contextual(ObjectIdHexStringSerializer)

    }
}

@Serializer(forClass = ObjectId::class)
object ObjectIdHexStringSerializer: KSerializer<ObjectId> {
    override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("ObjectIdHexStringSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ObjectId) {
        encoder.encodeString(value.toHexString())
    }

    override fun deserialize(decoder: Decoder): ObjectId {
        return ObjectId(decoder.decodeString())
    }
}
/**
 * based on Base64 URL Encoder and Decoder
 * */
@Serializer(forClass = ObjectId::class)
object ObjectIdBase64Serializer: KSerializer<ObjectId> {
    private val base64Decoder = Base64.getUrlDecoder()
    private val base64Encoder = Base64.getUrlEncoder()
    override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("ObjectIdBase64Serializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ObjectId) {
        encoder.encodeString(base64Encoder.encodeToString(value.toByteArray()))
    }

    override fun deserialize(decoder: Decoder): ObjectId {
        return ObjectId(base64Decoder.decode(decoder.decodeString()))
    }
}

fun main() {
    val id = ObjectId("5f6c8965ea79bd075df5becf")
    val bean = AuthBean(id,0, listOf("user"), "tokenXXXXXXX")
    val box = DataBox.ok(bean)
    println(apiJson.encodeToString(box))

    val str = Base64.getUrlEncoder().encodeToString(id.toByteArray())
    println("id=$str")//id=X2yJZep5vQdd9b7P
}