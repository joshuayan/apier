package cn.apier.auth.api

import cn.apier.auth.application.exception.ErrorDefinitions
import cn.apier.auth.application.service.AuthService
import cn.apier.auth.common.AuthTool
import cn.apier.auth.common.ConstantObject
import cn.apier.auth.query.entry.AuthTokenEntry
import cn.apier.auth.query.entry.UserEntry
import cn.apier.auth.query.repository.AuthTokenEntryRepository
import cn.apier.auth.query.service.AuthTokenEntryQueryService
import cn.apier.common.api.Result
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.ExecuteTool
import cn.apier.common.util.Utils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthApi {


    private val LOGGER = LoggerFactory.getLogger(AuthApi::class.java)

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
    fun signIn(mobile: String, password: String): Result<Any> = ExecuteTool.executeWithTry {
        parameterRequired(mobile, ConstantObject.STR_MOBILE)
        parameterRequired(password, ConstantObject.STR_PASSWORD)

        this.authService.signIn(mobile, password)
    }


    @GetMapping("/mockPara")
    fun mockClientRequestPara(): String {
        val appKey = "effbc5ff1de61716b474e1e1638276"
        val timestampInMs = System.currentTimeMillis()
        val appSecret = "bca2402e8b1b20f225eed04898a07d"
        val strToSign = appKey + appSecret + timestampInMs
        val signed = Utils.md5(strToSign)
        return "appKey:$appKey,timestamp:$timestampInMs,signature:$signed"
    }


    @GetMapping("/bdappinfo")
    fun queryBDApplicationInfo(): Result<BDApplicationInfo> = Result.OK(BDApplicationInfo(ConstantObject.BD_APP_KEY, ConstantObject.BD_SECRET_KEY))

    /**
     * 检查token是否有效
     *
     */

    @GetMapping("/checkToken")
    fun checkIfValidToken(token: String): Result<Boolean> = ExecuteTool.executeQueryWithTry {
        this.authService.checkIfValidToken(token)
    }


    @GetMapping("/checkSigned")
    fun checkIfSigned(token: String): Result<Boolean> = ExecuteTool.executeQueryWithTry {
        AuthTool.checkIfSigned(token)
    }

    @GetMapping("/signedUser")
    fun querySignedUser(token: String): Result<String> = ExecuteTool.executeQueryWithTry {
        val mobile = AuthTool.signedUser(token)?.let { it as UserEntry }?.mobile ?: ""
        LOGGER.debug("querySignedUser,mobile:$mobile")
        mobile
    }

    @GetMapping("/error/needToken")
    fun needToken(): Result<Any> = Result.FAIL(ErrorDefinitions.CODE_AUTH_NEED_TOKEN, ErrorDefinitions.MSG_AUTH_NEED_TOKEN)

    @GetMapping("/error/needSignIn")
    fun needSignIn(): Result<Any> = Result.FAIL(ErrorDefinitions.CODE_AUTH_NEED_SIGNIN, ErrorDefinitions.MSG_AUTH_NEED_SIGNIN)
}