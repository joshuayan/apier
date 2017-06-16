package cn.apier.standard.application.service

import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.UUIDUtil
import cn.apier.standard.application.command.CreateCountryCommand
import cn.apier.standard.application.command.UpdateCountryCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by yanjunhua on 2017/6/12.
 */
@Service
class RegionService {

    @Autowired
    lateinit var commandGateway: CommandGateway


    fun addNewCountry(name: String, description: String?) {
        commandGateway.sendAndWait<Unit>(CreateCountryCommand(UUIDUtil.commonUUID(), name, DateTimeUtil.now(), description))
    }

    fun updateCountry(id: String, name: String, description: String?) {
        commandGateway.sendAndWait<Unit>(UpdateCountryCommand(id, name, description))
    }
}