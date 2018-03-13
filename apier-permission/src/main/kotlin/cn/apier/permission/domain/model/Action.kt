package cn.apier.permission.domain.model

import cn.apier.common.domain.model.EnterpriseEnabledBaseModel
import cn.apier.permission.domain.event.ActionCreated
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class Action : EnterpriseEnabledBaseModel {

    private lateinit var name: String
    private lateinit var remark: String

    private constructor()
    constructor(name: String, remark: String, createdAt: Date) {
        AggregateLifecycle.apply(ActionCreated(uid, name, remark, createdAt))
    }


    @EventSourcingHandler
    private fun onCreated(actionCreated: ActionCreated) {
        this.uid = actionCreated.uid
        this.name = actionCreated.name
        this.remark = actionCreated.remark
        this.createdAt = actionCreated.createdAt
    }
}