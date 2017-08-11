package cn.apier.auth.application.command.handler

import cn.apier.auth.application.command.CreateAuthTokenCommand
import cn.apier.auth.application.exception.ErrorDefinitions
import cn.apier.auth.common.AuthConfig
import cn.apier.auth.common.ConstantObject
import cn.apier.auth.domain.model.AuthToken
import cn.apier.auth.query.repository.ClientApplicationEntryRepository
import cn.apier.common.exception.BaseException
import cn.apier.common.extension.validationRules
import cn.apier.common.util.UUIDUtil
import cn.apier.common.util.Utils
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthTokenCommandHandler {

    @Autowired
    private lateinit var configuration: Configuration
    @Autowired
    private lateinit var authConfig: AuthConfig

    @Autowired
    private lateinit var clientApplicationEntryRepository: ClientApplicationEntryRepository


    @CommandHandler
    fun processCreate(createAuthTokenCommand: CreateAuthTokenCommand) {

        val clientApplicationEntry = this.clientApplicationEntryRepository.findByAppKey(createAuthTokenCommand.appKey)

        validationRules {
            rule {
                ifNull { clientApplicationEntry }
                exception { BaseException(ErrorDefinitions.CODE_AUTH_INVALID_APPKEY, ErrorDefinitions.MSG_AUTH_INVALID_APPKEY) }
            }
        }.validate()

        val token = this.buildToken(createAuthTokenCommand.appKey)

        var expiredAt = Date(System.currentTimeMillis() + authConfig.tokenExpiredTimeInMs)


        configuration.repository(AuthToken::class.java).newInstance {
            AuthToken(createAuthTokenCommand.enterpriseId, UUIDUtil.commonUUID()
                    , token, clientApplicationEntry!!.uid, createAuthTokenCommand.appKey, expiredAt)
        }
    }


    private fun buildToken(appKey: String): String {
        val rawStr = appKey + System.currentTimeMillis() + ConstantObject.SALT_TOKEN
        val token = Utils.md5(rawStr)
        return token
    }

}