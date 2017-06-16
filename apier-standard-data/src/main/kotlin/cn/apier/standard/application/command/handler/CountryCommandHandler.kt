package cn.apier.standard.application.command.handler

import cn.apier.common.util.ExecuteTool
import cn.apier.standard.application.command.CreateCountryCommand
import cn.apier.standard.application.command.UpdateCountryCommand
import cn.apier.standard.domain.model.Country
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/15.
 */

@Component
open class CountryCommandHandler {

    @Autowired
    lateinit var configuration: Configuration

    @CommandHandler
    fun processCreation(createCountryCommand: CreateCountryCommand) {
        this.configuration.repository(Country::class.java).newInstance { Country(createCountryCommand.id, createCountryCommand.name, createCountryCommand.description) }
    }

    @CommandHandler
    fun processUpdate(updateCountryCommand: UpdateCountryCommand) {

        this.configuration.repository(Country::class.java).load(updateCountryCommand.id).execute {
            it.update(updateCountryCommand.id, updateCountryCommand.name, updateCountryCommand.description)
        }
    }

}