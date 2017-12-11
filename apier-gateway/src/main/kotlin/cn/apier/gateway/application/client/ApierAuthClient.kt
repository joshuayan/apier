package cn.apier.gateway.application.client

import cn.apier.common.api.Result
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@FeignClient("apier-auth")
interface ApierAuthClient {
    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/auth/checkToken")
    fun checkIfValidToken(@RequestParam("token") token: String, @RequestParam("expiredAt") expiredAt: Date): Result<Boolean>

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/auth/checkSigned")
    fun checkIfSigned(@RequestParam("token") token: String): Result<Boolean>

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/auth/signedUser")
    fun querySignedUser(@RequestParam("token") token: String): Result<String>
}