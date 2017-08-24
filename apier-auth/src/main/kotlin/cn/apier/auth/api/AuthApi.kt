package cn.apier.auth.api

import cn.apier.auth.application.exception.ErrorDefinitions
import cn.apier.auth.application.service.AuthService
import cn.apier.auth.common.ConstantObject
import cn.apier.auth.query.entry.AuthTokenEntry
import cn.apier.auth.query.repository.AuthTokenEntryRepository
import cn.apier.auth.query.service.AuthTokenEntryQueryService
import cn.apier.common.api.Result
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.ExecuteTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthApi {

    @Autowired
    private lateinit var authService: AuthService
    @Autowired
    private lateinit var authTokenEntryQueryService: AuthTokenEntryQueryService
    @Autowired
    private lateinit var authTokenEntryRepository: AuthTokenEntryRepository

    @GetMapping("/token")
    fun token(appKey: String, timestamp: String, signature: String): Result<Any> = ExecuteTool.executeQueryWithTry {
        var result: AuthTokenEntry
        this.authService.checkSignature(appKey, timestamp, signature)
        val tokenEntry = this.authTokenEntryQueryService.queryValidTokenByAppKey(appKey)
        when (tokenEntry) {
            null -> {
                this.authService.newToken(appKey, timestamp, signature)
                result = this.authTokenEntryQueryService.queryValidTokenByAppKey(appKey)!!
            }
            else -> result = tokenEntry
        }
        result
    }


    @PostMapping("/signIn")
    fun signIn(mobile: String, password: String) {
        parameterRequired(mobile, ConstantObject.STR_MOBILE)
        parameterRequired(password, ConstantObject.STR_PASSWORD)

        this.authService.signIn(mobile, password)
    }


    @GetMapping("/error/needToken")
    fun needToken(): Result<Any> = Result.FAIL(ErrorDefinitions.CODE_AUTH_NEED_TOKEN, ErrorDefinitions.MSG_AUTH_NEED_TOKEN)

    @GetMapping("/error/needSignIn")
    fun needSignIn(): Result<Any> = Result.FAIL(ErrorDefinitions.CODE_AUTH_NEED_SIGNIN, ErrorDefinitions.MSG_AUTH_NEED_SIGNIN)
}