package cn.apier.standard.application.command.handler

import cn.apier.common.util.ExecuteTool
import cn.apier.standard.application.command.CreateCityCommand
import cn.apier.standard.application.command.UpdateCityCommand
import cn.apier.standard.domain.model.City
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/20.
 */

@Component
open class CityCommandHandler {

    @Autowired
    lateinit var configuration: Configuration

    @CommandHandler
    fun processCreate(createCityCommand: CreateCityCommand) {
        this.configuration.repository(City::class.java).newInstance { City(createCityCommand.uid, createCityCommand.provinceId, createCityCommand.name,createCityCommand.code, createCityCommand.description) }
    }


    @CommandHandler
    fun processUpdate(updateCityCommand: UpdateCityCommand) {
        this.configuration.repository(City::class.java).load(updateCityCommand.uid).execute { it.update(updateCityCommand.name, updateCityCommand.provinceId,updateCityCommand.code, updateCityCommand.description) }
    }

}