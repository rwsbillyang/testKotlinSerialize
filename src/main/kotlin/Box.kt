import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * 抽象基类，包含了code和msg， API返回结果数据结构
 * @param code 错误码信息，正确则为"OK"，普通错误为"KO"
 * @param msg 错误信息，
 * */
@Serializable
abstract class Box(
    var code: String,
    var msg: String?
){
    constructor(): this(OK, null)
    companion object{
        const val OK = "OK"
        const val KO = "KO"
        const val NeedLogin = "NeedLogin"
    }
}

/**
 * 简化版的data class，可直接使用
 * 列表数据应该继承自Box：
 * data class ProjectListDataBox(val list: List<Project>? = null, val total: Int = 0): Box()
 *
 * @param data 返回的负载数据 response payload
 * */
@Serializable
//data class DataBox(@ContextualSerialization val data: Any?) : Box(OK,null)
data class DataBox(@Contextual val data: Any?) : Box(OK,null)
{
    constructor(): this(null)

    companion object{
        fun ok(data: Any?) = DataBox(data)
        fun ko(msg: String) = DataBox(null).apply { code = KO }
        fun needLogin(msg: String) = DataBox(null).apply { code = NeedLogin }
    }
}




/**
 * 抽象基类，在Box（包含code和msg）基础上额外添加了调试信息
 *
 *  Box的umi request版本， 额外添加了type，traceId， host等Troubleshooting信息
 * @param type showType: error display type： 0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
 * @param tId traceId: Convenient for back-end Troubleshooting: unique request ID
 * @param host Convenient for backend Troubleshooting: host of current access server
 * 参照 umi request需要的返回结果
 * https://umijs.org/plugins/plugin-request
 * */
@Serializable
abstract class UmiBox(
    var type: Int?,
    var tId: String?,
    var host: String?
): Box(OK, null)
{
    constructor(): this(null,null, null)
    companion object{
        const val SILENT = 0 // 不提示错误
        const val WARN_MESSAGE = 1 // 警告信息提示
        const val ERROR_MESSAGE = 2 // 错误信息提示
        const val NOTIFICATION = 4 // 通知提示
        const val REDIRECT = 9 // 页面跳转
    }
}

//@Serializable
//class ProjectListDataBox(val data: List<Project>? = null, val total: Int = 0): UmiBox()


/**
 * umi-request版本的databox
 * 除了列表外，可直接使用，列表使用方法如下：
 * data class ProjectListDataBox(val list: List<Project>? = null, val total: Int = 0): UmiBox()
 * @param data response payload
 * */
@Serializable
data class UmiDataBox(@Contextual val data: Any?)
    : UmiBox( null,null, null)
{
    constructor(): this(null)

    companion object{
        fun ok(data: Any?) = UmiDataBox(data)
        fun ko(msg: String, type: Int = WARN_MESSAGE) = UmiDataBox(null).apply { code = KO }
        fun needLogin(msg: String, type: Int = REDIRECT) = UmiDataBox(null).apply { code = NeedLogin }
    }
}

