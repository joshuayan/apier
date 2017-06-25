package cn.apier.mm.domain.model

import cn.apier.common.domain.model.EnabledBaseModel
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.ExecuteTool
import cn.apier.mm.domain.event.DeliveryManCreatedEvent
import cn.apier.mm.domain.event.DeliveryManDisabledEvent
import cn.apier.mm.domain.event.DeliveryManEnabledEvent
import cn.apier.mm.domain.event.DeliveryManUpdatedEvent
import com.github.stuxuhai.jpinyin.PinyinHelper
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler

/**
 * Created by yanjunhua on 2017/6/22.
 */
class DeliveryMan : EnabledBaseModel {
    private var name: String = ""
    private var description: String? = null
    private var mobile: String = ""
    private var mnemonicCode: String = ""


    private constructor()

    constructor(uid: String, name: String, mobile: String, enabled: Boolean, description: String?) {
        parameterRequired(uid, "uid")
        parameterRequired(name, "name")
        parameterRequired(mobile, "mobile")
        val mnemonicCode = PinyinHelper.getShortPinyin(name)
        AggregateLifecycle.apply(DeliveryManCreatedEvent(uid, name, mobile, mnemonicCode, enabled, DateTimeUtil.now(), description))
    }

    fun update(name: String, mobile: String, description: String?) {
        parameterRequired(name, "name")
        parameterRequired(mobile, "mobile")

        val mnemonicCode = PinyinHelper.getShortPinyin(name)
        AggregateLifecycle.apply(DeliveryManUpdatedEvent(uid, name, mobile, mnemonicCode, description))
    }


    fun enable() {
        ExecuteTool.invalidOperationIf { this.enabled }
        AggregateLifecycle.apply(DeliveryManEnabledEvent(uid, name))
    }

    fun disable() {
        ExecuteTool.invalidOperationIf { !this.enabled }
        AggregateLifecycle.apply(DeliveryManDisabledEvent(uid, name))
    }

    @EventSourcingHandler
    private fun onEnabled(deliveryManEnabledEvent: DeliveryManEnabledEvent) {
        this.enabled = true
    }

    @EventSourcingHandler
    private fun onDisabled(deliveryManDisabledEvent: DeliveryManDisabledEvent) {
        this.enabled = false
    }

    @EventSourcingHandler
    private fun onUpdated(deliveryManUpdatedEvent: DeliveryManUpdatedEvent) {
        this.name = deliveryManUpdatedEvent.name
        this.mobile = deliveryManUpdatedEvent.mobile
        this.description = deliveryManUpdatedEvent.description
        this.mnemonicCode = deliveryManUpdatedEvent.mnemonicCode
    }

    @EventSourcingHandler
    private fun onCreation(deliveryManCreatedEvent: DeliveryManCreatedEvent) {
        this.uid = deliveryManCreatedEvent.uid
        this.name = deliveryManCreatedEvent.name
        this.description = deliveryManCreatedEvent.description
        this.mnemonicCode = deliveryManCreatedEvent.mnemonicCode
        this.mobile = deliveryManCreatedEvent.mobile
        this.enabled = deliveryManCreatedEvent.enabled
    }

}