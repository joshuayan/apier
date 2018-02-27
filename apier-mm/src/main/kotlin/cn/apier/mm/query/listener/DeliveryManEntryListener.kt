package cn.apier.mm.query.listener

import cn.apier.common.exception.CommonException
import cn.apier.common.extension.invalidOperationIfNull
import cn.apier.mm.domain.event.DeliveryManCreatedEvent
import cn.apier.mm.domain.event.DeliveryManDisabledEvent
import cn.apier.mm.domain.event.DeliveryManEnabledEvent
import cn.apier.mm.domain.event.DeliveryManUpdatedEvent
import cn.apier.mm.query.dao.DeliveryManEntryRepository
import cn.apier.mm.query.entry.DeliveryManEntry
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/22.
 */
@Component
class DeliveryManEntryListener {

    @Autowired
    lateinit var deliveryManEntryRepository: DeliveryManEntryRepository

    @EventHandler
    fun onCreation(deliveryManCreatedEvent: DeliveryManCreatedEvent) {
        DeliveryManEntry(deliveryManCreatedEvent.uid, deliveryManCreatedEvent.name, deliveryManCreatedEvent.mobile, deliveryManCreatedEvent.mnemonicCode, deliveryManCreatedEvent.enabled
                , deliveryManCreatedEvent.createdAt, deliveryManCreatedEvent.description).also { this.deliveryManEntryRepository.save(it) }
    }

    @EventHandler
    fun onUpdated(deliveryManUpdatedEvent: DeliveryManUpdatedEvent) {
        this.deliveryManEntryRepository.findById(deliveryManUpdatedEvent.uid).orElseThrow { CommonException.invalidOperation() }
                .also {
                    it.description = deliveryManUpdatedEvent.description;it.mnemonicCode = deliveryManUpdatedEvent.mnemonicCode;
                    it.mobile = deliveryManUpdatedEvent.mobile;it.name = deliveryManUpdatedEvent.name
                }.also { this.deliveryManEntryRepository.save(it) }
    }


    @EventHandler
    fun onEnabled(deliveryManEnabledEvent: DeliveryManEnabledEvent) {
        this.deliveryManEntryRepository.findById(deliveryManEnabledEvent.uid).orElseThrow { CommonException.invalidOperation() }
                .also { it.enabled = true }
                .also { this.deliveryManEntryRepository.save(it) }
    }

    @EventHandler
    fun onDisabled(deliveryManDisabledEvent: DeliveryManDisabledEvent) {
        this.deliveryManEntryRepository.findById(deliveryManDisabledEvent.uid).orElseThrow { CommonException.invalidOperation() }
                .also { it.enabled = false }
                .also { this.deliveryManEntryRepository.save(it) }
    }
}