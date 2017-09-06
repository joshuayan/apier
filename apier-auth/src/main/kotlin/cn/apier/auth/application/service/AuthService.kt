package cn.apier.auth.application.service

import cn.apier.auth.application.command.CreateAuthTokenCommand
import cn.apier.auth.application.exception.ErrorDefinitions
import cn.apier.auth.common.AuthConfig
import cn.apier.auth.common.AuthTool
import cn.apier.auth.query.repository.AuthTokenEntryRepository
import cn.apier.auth.query.repository.ClientApplicationEntryRepository
import cn.apier.auth.query.repository.UserEntryRepository
import cn.apier.common.cache.MemoryCache
import cn.apier.common.exception.BaseException
import cn.apier.common.exception.CommonException
import cn.apier.common.extension.validationRules
import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.ExecuteTool
import cn.apier.common.util.Utils
import cn.apier.gateway.context.ApiGatewayContext
import com.sun.org.apache.xpath.internal.operations.Bool
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService {
    @Autowired
    private lateinit var authConfig: AuthConfig

    @Autowired
    private lateinit var commandGateway: CommandGateway
    @Autowired
    private lateinit var clientApplicationEntryRepository: ClientApplicationEntryRepository

    @Autowired
    private lateinit var userEntryRepository: UserEntryRepository

    @Autowired
    private lateinit var authTokenEntryRepository: AuthTokenEntryRepository

    fun newToken(appKey: String, timestamp: String, signature: String) {

        val enterpriseId: String = "78"

        ExecuteTool.loadAndThen { this.clientApplicationEntryRepository.findByAppKey(appKey) }.ifNonNull().exception { BaseException(ErrorDefinitions.CODE_AUTH_INVALID_APPKEY, ErrorDefinitions.MSG_AUTH_INVALID_APPKEY) }
                .then {
                    val appSecret = it?.appSecret!!
                    ExecuteTool.conditionalException({ !this.validateSignature(appKey, timestamp, signature, appSecret) }, { BaseException(ErrorDefinitions.CODE_AUTH_BAD_SIGNATURE, ErrorDefinitions.MSG_AUTH_BAD_SIGNATURE) })

                    val command = CreateAuthTokenCommand(enterpriseId, appKey)
                    commandGateway.sendAndWait<Unit>(command)
                }.execute()
    }


    fun checkSignature(appKey: String, timestampInMs: String, signature: String): Boolean {

        var result = false

        ExecuteTool.loadAndThen { this.clientApplicationEntryRepository.findByAppKey(appKey) }.ifNonNull().exception { BaseException(ErrorDefinitions.CODE_AUTH_INVALID_APPKEY, ErrorDefinitions.MSG_AUTH_INVALID_APPKEY) }
                .then {
                    val appSecret = it?.appSecret!!
                    ExecuteTool.conditionalException({ !this.validateSignature(appKey, timestampInMs, signature, appSecret) }, { BaseException(ErrorDefinitions.CODE_AUTH_BAD_SIGNATURE, ErrorDefinitions.MSG_AUTH_BAD_SIGNATURE) })
                    result = true
                }

        return result
    }


    fun signIn(mobile: String, password: String) {
//        parameterRequired(mobile, ConstantObject.STR_MOBILE)
//        parameterRequired(password, ConstantObject.STR_PASSWORD)

        validationRules {
            rule {
                ifTrue { mobile.isBlank() }
                exception { CommonException.invalidOperation() }
            }
            rule {
                ifTrue { password.isBlank() }
                exception { CommonException.invalidOperation() }
            }
        }.validate()


        val userEntry = Optional.ofNullable(this.userEntryRepository.findByMobile(mobile))
                .orElseThrow { BaseException(ErrorDefinitions.CODE_AUTH_SIGNIN_FAILED, ErrorDefinitions.MSG_SIGNIN_FAILED) }


        val encryptedPwd = AuthTool.encryptPwd(mobile, password)

        if (encryptedPwd == userEntry.password) {
            MemoryCache.default.cache(AuthTool.keyForSignedUser(ApiGatewayContext.currentContext.currentToken()!!), userEntry)
            println("signed in")
        }

    }


    private fun validateSignature(appKey: String, timestampInMs: String, signature: String, appSecret: String): Boolean {

        val strToSign = appKey + appSecret + timestampInMs
        val signed = Utils.md5(strToSign)
        return signed == signature
    }

    fun checkIfValidToken(token: String): Boolean {

        val authTokenEntry = this.authTokenEntryRepository.findByCodeAndExpiredAtGreaterThan(token, DateTimeUtil.now())

        return authTokenEntry != null

    }

}