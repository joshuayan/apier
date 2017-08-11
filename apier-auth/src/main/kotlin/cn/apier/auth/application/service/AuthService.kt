package cn.apier.auth.application.service

import cn.apier.auth.application.command.CreateAuthTokenCommand
import cn.apier.auth.application.exception.ErrorDefinitions
import cn.apier.auth.common.AuthConfig
import cn.apier.auth.query.repository.ClientApplicationEntryRepository
import cn.apier.common.exception.BaseException
import cn.apier.common.util.ExecuteTool
import cn.apier.common.util.Utils
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthService {
    @Autowired
    private lateinit var authConfig: AuthConfig

    @Autowired
    private lateinit var commandGateway: CommandGateway
    @Autowired
    private lateinit var clientApplicationEntryRepository: ClientApplicationEntryRepository

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

    private fun validateSignature(appKey: String, timestampInMs: String, signature: String, appSecret: String): Boolean {

        val strToSign = appKey + appSecret + timestampInMs
        val signed = Utils.md5(strToSign)
        return signed.equals(signature)
    }

}