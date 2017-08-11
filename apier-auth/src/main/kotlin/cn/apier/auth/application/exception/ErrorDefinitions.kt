package cn.apier.auth.application.exception

object ErrorDefinitions {
    val CODE_MOBILE_DUPLICATED = "USR_MOBILE_DUPLICATED"
    val CODE_AUTH_BAD_SIGNATURE = "AUTH_BAD_SIGNATURE"
    val CODE_AUTH_INVALID_APPKEY = "AUTH_INVALID_APPKEY"


    val MSG_MOBILE_DUPLICATED = "手机号已经注册，请直接登录"

    val MSG_AUTH_BAD_SIGNATURE = "签名不匹配，请参考文档"
    val MSG_AUTH_INVALID_APPKEY = "无效的AppKey"
}