package cn.apier.standard.application.command.handler

import cn.apier.standard.application.command.CreateDistrictCommand
import cn.apier.standard.application.command.UpdateDistrictCommand
import cn.apier.standard.domain.model.District
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/20.
 */


@Component
open class DistrictCommandHandler {

    @Autowired
    lateinit var configuration: Configuration


    @CommandHandler
    fun processCreate(createDistrictCommand: CreateDistrictCommand) {
        this.configuration.repository(District::class.java).newInstance { District(createDistrictCommand.uid, createDistrictCommand.cityId, createDistrictCommand.name, createDistrictCommand.description) }
    }

    @CommandHandler
    fun processUpdate(updateDistrictCommand: UpdateDistrictCommand) {
        this.configuration.repository(District::class.java).load(updateDistrictCommand.uid).execute {
            it.update(updateDistrictCommand.cityId, updateDistrictCommand.name, updateDistrictCommand.description)
        }
    }
}