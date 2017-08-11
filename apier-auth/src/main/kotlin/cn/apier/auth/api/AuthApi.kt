package cn.apier.auth.api

import cn.apier.auth.application.service.AuthService
import cn.apier.auth.query.entry.AuthTokenEntry
import cn.apier.auth.query.repository.AuthTokenEntryRepository
import cn.apier.auth.query.service.AuthTokenEntryQueryService
import cn.apier.common.api.Result
import cn.apier.common.util.ExecuteTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthApi {

    @Autowired
    private lateinit var tokenService: AuthService
    @Autowired
    private lateinit var authTokenEntryQueryService: AuthTokenEntryQueryService
    @Autowired
    private lateinit var authTokenEntryRepository: AuthTokenEntryRepository

    @GetMapping("/token")
    fun token(appKey: String, timestamp: String, signature: String): Result<Any> = ExecuteTool.executeQueryWithTry {
        var result: AuthTokenEntry
        this.tokenService.checkSignature(appKey, timestamp, signature)
        val tokenEntry = this.authTokenEntryQueryService.queryValidTokenByAppKey(appKey)
        when (tokenEntry) {
            null -> {
                this.tokenService.newToken(appKey, timestamp, signature)
                result = this.authTokenEntryQueryService.queryValidTokenByAppKey(appKey)!!
            }
            else -> result = tokenEntry
        }
        result
    }
}