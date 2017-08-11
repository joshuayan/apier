package cn.apier.auth.api

import cn.apier.auth.application.service.UserService
import cn.apier.common.api.Result
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.ExecuteTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserApi {
    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/register")
    fun register(mobile: String, password: String, validCode: String): Result<Any> = ExecuteTool.executeWithTry {
        parameterRequired(mobile, "mobile")
        parameterRequired(password, "password")
        parameterRequired(validCode, "validCode")
        this.userService.register(mobile, password)
    }
}