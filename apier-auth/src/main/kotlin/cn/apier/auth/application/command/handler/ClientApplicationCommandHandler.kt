package cn.apier.auth.application.command.handler

import cn.apier.auth.application.command.CreateClientApplicationCommand
import cn.apier.auth.domain.model.ClientApplication
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ClientApplicationCommandHandler {

    @Autowired
    private lateinit var configuration: Configuration

    @CommandHandler
    fun processCreate(createClientApplicationCommand: CreateClientApplicationCommand) {
        configuration.repository(ClientApplication::class.java).newInstance {
            ClientApplication(createClientApplicationCommand.enterpriseId, createClientApplicationCommand.uid
                    , createClientApplicationCommand.name, createClientApplicationCommand.appKey, createClientApplicationCommand.appSecret
                    , createClientApplicationCommand.deleted, createClientApplicationCommand.createdAt)
        }
    }
}