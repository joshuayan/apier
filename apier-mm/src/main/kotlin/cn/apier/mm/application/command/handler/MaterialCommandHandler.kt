package cn.apier.mm.application.command.handler

import cn.apier.mm.application.command.CreateMaterialCommand
import cn.apier.mm.application.command.DisableMaterialCommand
import cn.apier.mm.application.command.EnableMaterialCommand
import cn.apier.mm.application.command.UpdateMaterialCommand
import cn.apier.mm.domain.model.Material
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/22.
 */


@Component
open class MaterialCommandHandler {

    @Autowired
    lateinit var configuration: Configuration

    @CommandHandler
    fun processCreation(createMaterialCommand: CreateMaterialCommand) {
        this.configuration.repository<Material>(Material::class.java).newInstance { Material(createMaterialCommand.uid, createMaterialCommand.code, createMaterialCommand.name, createMaterialCommand.categoryId, createMaterialCommand.enabled, createMaterialCommand.description) }
    }


    @CommandHandler
    fun processUpdate(updateMaterialCommand: UpdateMaterialCommand) {
        this.configuration.repository(Material::class.java).load(updateMaterialCommand.uid).execute { it.update(updateMaterialCommand.name, updateMaterialCommand.categoryId, updateMaterialCommand.description) }
    }

    @CommandHandler
    fun processEnable(enableMaterialCommand: EnableMaterialCommand) {
        this.configuration.repository(Material::class.java).load(enableMaterialCommand.uid).execute { it.enable() }
    }

    @CommandHandler
    fun processDisable(disableMaterialCommand: DisableMaterialCommand) {
        this.configuration.repository(Material::class.java).load(disableMaterialCommand.uid).execute { it.disable() }

    }
}