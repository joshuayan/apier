package cn.apier.mm.application.service

import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.UUIDUtil
import cn.apier.mm.application.command.CreateMaterialCommand
import cn.apier.mm.application.command.DisableMaterialCommand
import cn.apier.mm.application.command.EnableMaterialCommand
import cn.apier.mm.application.command.UpdateMaterialCommand
import cn.apier.mm.domain.model.BusinessScope
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by yanjunhua on 2017/6/22.
 */

@Service
open class MaterialService {

    @Autowired
    lateinit var commandGateway: CommandGateway

    fun newMaterial(tenantId: String, name: String, code: String, categoryId: String, enabled: Boolean, businessScope: String, description: String?) {
        parameterRequired(name, "name")
        parameterRequired(code, "code")
        parameterRequired(categoryId, "categoryId")
        this.commandGateway.sendAndWait<Unit>(CreateMaterialCommand(UUIDUtil.commonUUID(), tenantId, name, code, categoryId, enabled, businessScope, description))
    }

    fun updateMaterial(uid: String, name: String, categoryId: String, description: String?) {
        parameterRequired(uid, "uid")
        parameterRequired(name, "name")
        parameterRequired(categoryId, "categoryId")
        this.commandGateway.sendAndWait<Unit>(UpdateMaterialCommand(uid, name, categoryId, description))
    }

    fun enableMaterial(uid: String) {
        parameterRequired(uid, "uid")
        this.commandGateway.sendAndWait<Unit>(EnableMaterialCommand(uid))
    }

    fun disableMaterial(uid: String) {
        parameterRequired(uid, "uid")
        this.commandGateway.sendAndWait<Unit>(DisableMaterialCommand(uid))
    }
}