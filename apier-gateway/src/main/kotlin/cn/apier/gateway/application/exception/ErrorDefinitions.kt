package cn.apier.gateway.application.exception

object ErrorDefinitions {
    val CODE_MOBILE_DUPLICATED = "USR_MOBILE_DUPLICATED"
    val CODE_AUTH_BAD_SIGNATURE = "AUTH_BAD_SIGNATURE"
    val CODE_AUTH_INVALID_APPKEY = "AUTH_INVALID_APPKEY"
    val CODE_AUTH_INVALID_MOBILE = "AUTH_INVALID_MOBILE"
    val CODE_AUTH_SIGNIN_FAILED = "AUTH_SIGNIN_FAILED"
    val CODE_AUTH_NEED_TOKEN = "AUTH_NEED_TOKEN"
    val CODE_AUTH_NEED_SIGNIN = "AUTH_NEED_SIGNIN"


    val MSG_MOBILE_DUPLICATED = "手机号已经注册，请直接登录"
    val MSG_INVALID_MOBILE = "手机号已经注册，请直接登录"
    val MSG_SIGNIN_FAILED = "手机号或密码不正确，请重新输入"


    val MSG_AUTH_BAD_SIGNATURE = "签名不匹配，请参考文档"
    val MSG_AUTH_INVALID_APPKEY = "无效的AppKey"
    val MSG_AUTH_NEED_TOKEN = "请先获取token"
    val MSG_AUTH_NEED_SIGNIN = "请先登录"
}