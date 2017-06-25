package cn.apier.mm.application.service

import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.UUIDUtil
import cn.apier.mm.application.command.CreateDeliveryManCommand
import cn.apier.mm.application.command.DisableDeliveryManCommand
import cn.apier.mm.application.command.EnableDeliveryManCommand
import cn.apier.mm.application.command.UpdateDeliveryManCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by yanjunhua on 2017/6/22.
 */

@Service
open class DeliveryManService {

    @Autowired
    lateinit var commandGateway: CommandGateway

    fun newDeliveryMan(name: String, mobile: String, description: String?) {
        parameterRequired(name, "name")
        parameterRequired(mobile, "mobile")

        this.commandGateway.sendAndWait<Unit>(CreateDeliveryManCommand(UUIDUtil.commonUUID(), name, mobile, true, description))
    }

    fun updateDeliveryMan(uid: String, name: String, mobile: String, description: String?) {
        parameterRequired(uid, "uid")
        parameterRequired(name, "name")
        parameterRequired(mobile, "mobile")
        this.commandGateway.sendAndWait<Unit>(UpdateDeliveryManCommand(uid, name, mobile, description))
    }

    fun enableDeliveryMan(uid: String) {
        parameterRequired(uid, "uid")
        this.commandGateway.sendAndWait<Unit>(EnableDeliveryManCommand(uid))
    }


    fun disableDeliveryMan(uid: String) {
        parameterRequired(uid, "uid")
        this.commandGateway.sendAndWait<Unit>(DisableDeliveryManCommand(uid))
    }

}