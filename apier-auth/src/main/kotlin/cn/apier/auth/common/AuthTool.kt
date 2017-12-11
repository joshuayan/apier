package cn.apier.auth.common

import cn.apier.common.cache.MemoryCache
import cn.apier.common.util.Utils

object AuthTool {


    fun checkSignature(appKey: String, timestampInMs: String, signature: String, appSecret: String): Boolean {
        val strToSign = appKey + appSecret + timestampInMs
        val signed = Utils.md5(strToSign)
        return signed == signature
    }


    fun encryptPwd(mobile: String, password: String): String {
        val strToEncrypt = "$password${ConstantObject.SALT_PASSWORD}$mobile"
        return Utils.md5(strToEncrypt)
    }


    fun checkIfSigned(token: String) =
            MemoryCache.default.get(keyForSignedUser(token)) != null


    fun signedUser(token: String): Any? = MemoryCache.default.get(keyForSignedUser(token))

    fun keyForSignedUser(token: String) = ConstantObject.KEY_LOGIN_USER + token
}


object TokenHolder {
    private val mCurrentToken: ThreadLocal<String> = ThreadLocal.withInitial({ "NoToken" })
    fun setCurrentToken(token: String) {
        mCurrentToken.set(token)
    }

    fun currentToken() = mCurrentToken.get()


}