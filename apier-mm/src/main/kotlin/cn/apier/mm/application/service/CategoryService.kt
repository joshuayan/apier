package cn.apier.mm.application.service

import cn.apier.common.util.ExecuteTool
import cn.apier.common.util.UUIDUtil
import cn.apier.mm.application.command.CreateCategoryCommand
import cn.apier.mm.application.command.DisableCategoryCommand
import cn.apier.mm.application.command.EnableCategoryCommand
import cn.apier.mm.application.command.UpdateCategoryCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by yanjunhua on 2017/6/21.
 */
@Service
class CategoryService {
    @Autowired
    private lateinit var commandGateway: CommandGateway

    fun newCategory(name: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        this.commandGateway.sendAndWait<Unit>(CreateCategoryCommand(UUIDUtil.commonUUID(), name, true, description))
    }

    fun updateCategory(uid: String, name: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        this.commandGateway.sendAndWait<Unit>(UpdateCategoryCommand(uid, name, description))
    }

    fun disable(uid: String) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        this.commandGateway.sendAndWait<Unit>(DisableCategoryCommand(uid))
    }

    fun enable(uid: String) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        this.commandGateway.sendAndWait<Unit>(EnableCategoryCommand(uid))
    }

}