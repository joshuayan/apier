package cn.apier.permission.domain.model

import cn.apier.common.domain.model.EnterpriseEnabledBaseModel
import cn.apier.common.exception.CommonException
import cn.apier.common.extension.validationRules
import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.UUIDUtil
import cn.apier.permission.domain.event.ResourceActionAdded
import cn.apier.permission.domain.event.ResourceCreated
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class Resource : EnterpriseEnabledBaseModel {
    private lateinit var name: String
    private lateinit var remark: String
    private var actions: MutableList<String> = mutableListOf()

    private constructor()
    constructor(uid: String, name: String, remark: String) {
        AggregateLifecycle.apply(ResourceCreated(uid, name, remark, DateTimeUtil.now()))
    }

    fun addAction(actionId: String) {
        validationRules {
            rule {
                ifTrue { actions.contains(actionId) }
                exception { CommonException.invalidOperation() }
            }
        }.validate()
        AggregateLifecycle.apply(ResourceActionAdded(UUIDUtil.commonUUID(), this.uid, actionId, DateTimeUtil.now()))
    }


    @EventSourcingHandler
    private fun onActionAdded(resourceActionAdded: ResourceActionAdded) {
        this.actions.add(resourceActionAdded.actionId)
    }

    @EventSourcingHandler
    private fun onCreated(resourceCreated: ResourceCreated) {
        this.uid = resourceCreated.uid
        this.name = resourceCreated.name
        this.remark = resourceCreated.remark
        this.createdAt = resourceCreated.createdAt
    }
}