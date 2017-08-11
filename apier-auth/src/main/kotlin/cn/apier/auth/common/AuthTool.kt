package cn.apier.auth.common

import cn.apier.common.util.Utils

object AuthTool {

    fun checkSignature(appKey: String, timestampInMs: String, signature: String, appSecret: String): Boolean {
        val strToSign = appKey + appSecret + timestampInMs
        val signed = Utils.md5(strToSign)
        return signed.equals(signature)
    }

}