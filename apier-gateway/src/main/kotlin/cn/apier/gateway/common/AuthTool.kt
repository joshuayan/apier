package cn.apier.gateway.common

import cn.apier.common.cache.MemoryCache
import cn.apier.common.util.Utils

object AuthTool {


    fun checkSignature(appKey: String, timestampInMs: String, signature: String, appSecret: String): Boolean {
        val strToSign = appKey + appSecret + timestampInMs
        val signed = Utils.md5(strToSign)
        return signed == signature
    }


    fun checkIfSigned() =
            MemoryCache.default.get(keyForSignedUser()) != null


    fun signedUser() = MemoryCache.default.get(keyForSignedUser())

    fun keyForSignedUser() =ConstantObject.KEY_LOGIN_USER + TokenHolder.currentToken()
}

object TokenHolder {
    private val mCurrentToken: ThreadLocal<String> = ThreadLocal.withInitial({ "NoToken" })
    fun setCurrentToken(token: String) {
        mCurrentToken.set(token)
    }

    fun currentToken() = mCurrentToken.get()


}