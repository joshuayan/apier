package cn.apier.mm.domain.model

import cn.apier.common.domain.model.EnabledBaseModel
import cn.apier.common.domain.model.TenantedEnabledBaseModel
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.ExecuteTool
import cn.apier.mm.domain.event.CategoryCreatedEvent
import cn.apier.mm.domain.event.CategoryDisabledEvent
import cn.apier.mm.domain.event.CategoryEnabledEvent
import cn.apier.mm.domain.event.CategoryUpdatedEvent
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate

/**
 * Created by yanjunhua on 2017/6/21.
 */

@Aggregate
class Category : TenantedEnabledBaseModel {
    private constructor()
    constructor(tenantId: String, uid: String, name: String, enabled: Boolean, description: String?) {
        parameterRequired(tenantId, "tenantId")
        parameterRequired(uid, "uid")
        AggregateLifecycle.apply(CategoryCreatedEvent(tenantId, uid, name, enabled, createdAt, description))
    }

    private var name: String = ""
    private var description: String? = null


    fun update(name: String, description: String?) {
        ExecuteTool.invalidOperationIf { !this.enabled }
        AggregateLifecycle.apply(CategoryUpdatedEvent(uid, name, description))
    }

    fun enable() {
        ExecuteTool.invalidOperationIf { this.enabled }
        AggregateLifecycle.apply(CategoryEnabledEvent(uid, name))
    }

    fun disable() {
        ExecuteTool.invalidOperationIf { !this.enabled }
        AggregateLifecycle.apply(CategoryDisabledEvent(uid, name))
    }

    @EventSourcingHandler
    private fun onDisabled(categoryDisabledEvent: CategoryDisabledEvent) {
        this.enabled = false
    }

    @EventSourcingHandler
    private fun onEnabled(categoryEnabledEvent: CategoryEnabledEvent) {
        this.enabled = true
    }

    @EventSourcingHandler
    private fun onUpdated(categoryUpdatedEvent: CategoryUpdatedEvent) {
        this.name = categoryUpdatedEvent.name
        this.description = categoryUpdatedEvent.description
    }

    @EventSourcingHandler
    private fun onCreation(categoryCreatedEvent: CategoryCreatedEvent) {
        this.uid = categoryCreatedEvent.uid
        this.name = categoryCreatedEvent.name
        this.description = categoryCreatedEvent.description
        this.createdAt = categoryCreatedEvent.createdAt
        this.enabled = categoryCreatedEvent.enabled
        this.tenantId = categoryCreatedEvent.tenantId
    }

}