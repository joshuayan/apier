package cn.apier.mm.application.command.handler

import cn.apier.mm.application.command.CreateDeliveryManCommand
import cn.apier.mm.application.command.DisableDeliveryManCommand
import cn.apier.mm.application.command.EnableDeliveryManCommand
import cn.apier.mm.application.command.UpdateDeliveryManCommand
import cn.apier.mm.domain.model.DeliveryMan
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/22.
 */
@Component
open class DeliveryManCommandHandler {

    @Autowired
    lateinit var configuration: Configuration

    @CommandHandler
    fun processCreate(createDeliveryManCommand: CreateDeliveryManCommand) {
        this.configuration.repository(DeliveryMan::class.java).newInstance {
            DeliveryMan(createDeliveryManCommand.uid, createDeliveryManCommand.name
                    , createDeliveryManCommand.mobile, createDeliveryManCommand.enabled, createDeliveryManCommand.description)
        }
    }

    @CommandHandler
    fun processUpdate(updateDeliveryManCommand: UpdateDeliveryManCommand) {
        this.configuration.repository(DeliveryMan::class.java).load(updateDeliveryManCommand.uid).execute {
            it.update(updateDeliveryManCommand.name
                    , updateDeliveryManCommand.mobile, updateDeliveryManCommand.description)
        }
    }

    @CommandHandler
    fun processEnable(enableDeliveryManCommand: EnableDeliveryManCommand) {
        this.configuration.repository(DeliveryMan::class.java).load(enableDeliveryManCommand.uid).execute { it.enable() }
    }

    @CommandHandler
    fun processDisable(disableDeliveryManCommand: DisableDeliveryManCommand) {
        this.configuration.repository(DeliveryMan::class.java).load(disableDeliveryManCommand.uid).execute { it.disable() }

    }
}