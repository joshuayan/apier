package cn.apier.auth.application.command.handler

import cn.apier.auth.application.command.CreateUserCommand
import cn.apier.auth.application.command.UpdatePasswordCommand
import cn.apier.auth.domain.model.User
import cn.apier.common.extension.invalidOperationIfNull
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class UserCommandHandler {

    @Autowired
    private lateinit var configuration: Configuration

    @CommandHandler
    fun processCreation(createUserCommand: CreateUserCommand) {
        configuration.repository(User::class.java).newInstance { User(createUserCommand.uid, createUserCommand.mobile, createUserCommand.password, createUserCommand.createdAt) }
    }


    @CommandHandler
    fun processUpdatePassword(updatePasswordCommand: UpdatePasswordCommand) {
        configuration.repository(User::class.java).load(updatePasswordCommand.uid).execute {
            it.invalidOperationIfNull()
            it.updatePassword(updatePasswordCommand.password)
        }
    }

}