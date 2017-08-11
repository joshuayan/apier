package cn.apier.mm.application.service

import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.ExecuteTool
import cn.apier.common.util.UUIDUtil
import cn.apier.mm.application.command.CreateCategoryCommand
import cn.apier.mm.application.command.DisableCategoryCommand
import cn.apier.mm.application.command.EnableCategoryCommand
import cn.apier.mm.application.command.UpdateCategoryCommand
import cn.apier.mm.domain.model.Category
import cn.apier.mm.query.dao.CategoryEntryRepository
import cn.apier.mm.query.service.CategoryEntryQueryService
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by yanjunhua on 2017/6/21.
 */
@Service
class CategoryService {
    @Autowired
    private lateinit var commandGateway: CommandGateway
    @Autowired
    private lateinit var configuration: Configuration

    @Autowired
    private lateinit var categoryEntryQueryService: CategoryEntryQueryService
    @Autowired
    private lateinit var categoryEntryRepository: CategoryEntryRepository

    private val defaultTenantId:String="001"

    fun newCategory(name: String, description: String?) {
        parameterRequired(name, "name")
        ExecuteTool.invalidOperationIf { this.categoryEntryQueryService.checkIfDuplicatedName(defaultTenantId,name) }
        this.commandGateway.sendAndWait<Unit>(CreateCategoryCommand(defaultTenantId,UUIDUtil.commonUUID(), name, true, description))
    }

    fun updateCategory(uid: String, name: String, description: String?) {
        parameterRequired(uid, "uid")
        parameterRequired(name, "name")
        ExecuteTool.invalidOperationIf { !this.checkExists(uid) }
        ExecuteTool.invalidOperationIf { this.categoryEntryQueryService.checkIfDuplicatedNameExcludeId(name,defaultTenantId, uid) }
        this.commandGateway.sendAndWait<Unit>(UpdateCategoryCommand(uid, name, description))
    }

    fun disable(uid: String) {
        parameterRequired(uid, "uid")
        ExecuteTool.invalidOperationIf { !this.checkExists(uid) }
        this.commandGateway.sendAndWait<Unit>(DisableCategoryCommand(uid))
    }

    fun enable(uid: String) {
        parameterRequired(uid, "uid")
        ExecuteTool.invalidOperationIf { !this.checkExists(uid) }
        this.commandGateway.sendAndWait<Unit>(EnableCategoryCommand(uid))
    }

    private fun checkExists(uid: String): Boolean = this.configuration.repository(Category::class.java).load(uid).invoke { Objects.nonNull(it) }

}