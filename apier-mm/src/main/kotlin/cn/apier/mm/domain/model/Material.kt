package cn.apier.mm.domain.model

import cn.apier.common.domain.model.EnabledBaseModel
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.ExecuteTool
import cn.apier.mm.domain.event.MaterialCreatedEvent
import cn.apier.mm.domain.event.MaterialDisabledEvent
import cn.apier.mm.domain.event.MaterialEnabledEvent
import cn.apier.mm.domain.event.MaterialUpdatedEvent
import com.github.stuxuhai.jpinyin.PinyinHelper
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate

/**
 * Created by yanjunhua on 2017/6/22.
 */
@Aggregate
open class Material : EnabledBaseModel {

    private var name: String = ""
    private var code: String = ""
    private var description: String? = null
    private var categoryId: String = ""
    private var tenantId: String = ""
    private var businessScope: BusinessScope = BusinessScope.UNKNOWN
    /**
     * 助记码
     */
    private var mnemonicCode: String = ""

    private constructor()
    constructor(uid: String,  tenantId: String, name: String,code: String, categoryId: String, enabled: Boolean, businessScope: String, description: String?) {
        parameterRequired(uid, "uid")
        parameterRequired(code, "code")
        parameterRequired(name, "name")
        parameterRequired(tenantId, "enterpriseId")
        parameterRequired(categoryId, "categoryId")
        val mnemonicCode = PinyinHelper.getShortPinyin(name)
        val scope = BusinessScope.valueOf(businessScope)
        AggregateLifecycle.apply(MaterialCreatedEvent(tenantId, uid, code, name, categoryId, mnemonicCode, enabled, scope, DateTimeUtil.now(), description))
    }


    fun update(name: String, categoryId: String, description: String?) {
        parameterRequired(name, "name")
        parameterRequired(categoryId, "categoryId")
        ExecuteTool.invalidOperationIf { !this.enabled }
        val mnemonicCode = PinyinHelper.getShortPinyin(name)
        AggregateLifecycle.apply(MaterialUpdatedEvent(uid, code, name, categoryId, mnemonicCode, description))
    }

    fun enable() {
        ExecuteTool.invalidOperationIf { this.enabled }
        AggregateLifecycle.apply(MaterialEnabledEvent(uid, code))
    }

    fun disable() {
        ExecuteTool.invalidOperationIf { !this.enabled }
        AggregateLifecycle.apply(MaterialDisabledEvent(uid, code))
    }

    @EventSourcingHandler
    private fun onDisabled(materialDisabledEvent: MaterialDisabledEvent) {
        this.enabled = false
    }

    @EventSourcingHandler
    private fun onEnabled(materialEnabledEvent: MaterialEnabledEvent) {
        this.enabled = true
    }

    @EventSourcingHandler
    private fun onUpdated(materialUpdatedEvent: MaterialUpdatedEvent) {
        this.name = materialUpdatedEvent.name
        this.categoryId = materialUpdatedEvent.categoryId
        this.description = materialUpdatedEvent.description
        this.mnemonicCode = materialUpdatedEvent.mnemonicCode
    }

    @EventSourcingHandler
    private fun onCreation(materialCreatedEvent: MaterialCreatedEvent) {
        this.uid = materialCreatedEvent.uid
        this.name = materialCreatedEvent.name
        this.categoryId = materialCreatedEvent.categoryId
        this.code = materialCreatedEvent.code
        this.description = materialCreatedEvent.description
        this.createdAt = materialCreatedEvent.createdAt
        this.enabled = materialCreatedEvent.enabled
        this.mnemonicCode = materialCreatedEvent.mnemonicCode
        this.businessScope = materialCreatedEvent.bussinessScope
        this.tenantId = materialCreatedEvent.tenantId
    }

}