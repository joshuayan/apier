package cn.apier.auth

import cn.apier.common.util.Utils
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties
open class AuthApplication


fun main(args: Array<String>) {
    SpringApplication.run(AuthApplication::class.java, *args)
//    test()
}


private fun sign(appKey: String, timestampInMs: String, appSecret: String): String {

    val strToSign = appKey + appSecret + timestampInMs
    val signed = Utils.md5(strToSign)
    return signed
}

private fun test() {
    val timestamp = "1502356703351"
    val appKey = "31f695e8e1e8a5af16254693ebef98"
    val appSecret = "30cd184c2248dbfc4a1daaf02e33b2"

    val signed = sign(appKey, timestamp, appSecret)
    println("signed:$signed")
}
