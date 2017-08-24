package cn.apier.auth.domain.model

import cn.apier.auth.common.AuthTool
import cn.apier.auth.domain.event.UserCreatedEvent
import cn.apier.auth.domain.event.UserDisabled
import cn.apier.auth.domain.event.UserEnabled
import cn.apier.auth.domain.event.UserPasswordUpdatedEvent
import cn.apier.common.domain.model.EnabledBaseModel
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.ExecuteTool
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
open class User : EnabledBaseModel {

    private var mobile: String = ""
    private var password: String = ""


    private constructor()

    constructor(uid: String, mobile: String, password: String, createdAt: Date) {
        parameterRequired(uid, "uid")
        parameterRequired(mobile, "mobile")
        AggregateLifecycle.apply(UserCreatedEvent(uid, mobile, AuthTool.encryptPwd(mobile,password), createdAt, enabled))
    }


    fun updatePassword(password: String) {
        AggregateLifecycle.apply(UserPasswordUpdatedEvent(uid, AuthTool.encryptPwd(mobile,password)))
    }


    fun disable() {
        ExecuteTool.invalidOperationIf { !this.enabled }
        AggregateLifecycle.apply(UserDisabled(uid, this.mobile))
    }

    fun enable() {
        ExecuteTool.invalidOperationIf { this.enabled }
        AggregateLifecycle.apply(UserEnabled(uid, mobile))
    }

    @EventSourcingHandler
    private fun onEnabled(userEnabled: UserEnabled) {
        this.enabled=true
    }

    @EventSourcingHandler
    private fun onDisabled(userDisabled: UserDisabled) {
        this.enabled = false
    }


    @EventSourcingHandler
    private fun onPasswordUpdated(userPasswordUpdatedEvent: UserPasswordUpdatedEvent) {
        this.password = userPasswordUpdatedEvent.password
    }

    @EventSourcingHandler
    private fun onCreated(userCreatedEvent: UserCreatedEvent) {
        this.uid = userCreatedEvent.uid
        this.mobile = userCreatedEvent.mobile
        this.password = userCreatedEvent.password
        this.createdAt = userCreatedEvent.createdAt
        this.enabled = userCreatedEvent.enabled
    }


}