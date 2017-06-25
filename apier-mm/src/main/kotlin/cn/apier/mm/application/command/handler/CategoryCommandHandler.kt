package cn.apier.mm.application.command.handler

import cn.apier.mm.application.command.CreateCategoryCommand
import cn.apier.mm.application.command.DisableCategoryCommand
import cn.apier.mm.application.command.EnableCategoryCommand
import cn.apier.mm.application.command.UpdateCategoryCommand
import cn.apier.mm.domain.model.Category
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/21.
 */
@Component
class CategoryCommandHandler {
    @Autowired
    private lateinit var configuration: Configuration

    @CommandHandler
    fun processCreate(createCategoryCommand: CreateCategoryCommand) = this.configuration.repository(Category::class.java).newInstance { Category(createCategoryCommand.uid, createCategoryCommand.name, createCategoryCommand.description) }


    fun processUpdate(updateCategoryCommand: UpdateCategoryCommand) = this.configuration.repository(Category::class.java).load(updateCategoryCommand.uid).execute { it.update(updateCategoryCommand.name, updateCategoryCommand.description) }


    fun processEnable(enableCategoryCommand: EnableCategoryCommand) {
        this.configuration.repository(Category::class.java).load(enableCategoryCommand.uid).execute { it.enable() }
    }


    fun processDisable(disableCategoryCommand: DisableCategoryCommand) {
        this.configuration.repository(Category::class.java).load(disableCategoryCommand.uid).execute { it.disable() }
    }
}