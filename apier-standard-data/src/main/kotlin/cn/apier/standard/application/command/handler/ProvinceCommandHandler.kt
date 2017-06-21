package cn.apier.standard.application.command.handler

import cn.apier.standard.application.command.CreateProvinceCommand
import cn.apier.standard.application.command.UpdateProvinceCommand
import cn.apier.standard.domain.model.Province
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/20.
 */
@Component
open class ProvinceCommandHandler {
    @Autowired
    private lateinit var configuration: Configuration


    @CommandHandler
    fun processCreateCommand(createProvinceCommand: CreateProvinceCommand) {
        this.configuration.repository(Province::class.java).newInstance { Province(createProvinceCommand.uid, createProvinceCommand.countryId, createProvinceCommand.name,createProvinceCommand.code, createProvinceCommand.description) }
    }


    @CommandHandler
    fun processUpdateCommand(updateProvinceCommand: UpdateProvinceCommand) {
        this.configuration.repository(Province::class.java).load(updateProvinceCommand.uid).execute { it.update(updateProvinceCommand.name, updateProvinceCommand.countryId,updateProvinceCommand.code, updateProvinceCommand.description) }
    }
}