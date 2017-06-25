package cn.apier.mm.query.listener

import cn.apier.common.extension.invalidOperationIfNull
import cn.apier.mm.domain.event.MaterialCreatedEvent
import cn.apier.mm.domain.event.MaterialDisabledEvent
import cn.apier.mm.domain.event.MaterialEnabledEvent
import cn.apier.mm.domain.event.MaterialUpdatedEvent
import cn.apier.mm.query.dao.MaterialEntryRepository
import cn.apier.mm.query.entry.MaterialEntry
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/22.
 */
@Component
class MaterialEntryListener {

    @Autowired
    lateinit var materialEntryRepository: MaterialEntryRepository

    @EventHandler
    fun onCreated(materialCreatedEvent: MaterialCreatedEvent) {
        MaterialEntry(materialCreatedEvent.uid, materialCreatedEvent.code, materialCreatedEvent.name, materialCreatedEvent.categoryId,
                materialCreatedEvent.mnemonicCode, materialCreatedEvent.enabled, materialCreatedEvent.createdAt, materialCreatedEvent.description)
                .also { this.materialEntryRepository.save(it) }
    }

    @EventHandler
    fun onUpdated(materialUpdatedEvent: MaterialUpdatedEvent) {
        this.materialEntryRepository.findOne(materialUpdatedEvent.uid).invalidOperationIfNull()
                .also {
                    it.name = materialUpdatedEvent.name;it.categoryId = materialUpdatedEvent.categoryId;it.description = materialUpdatedEvent.description
                    it.mnemonicCode = materialUpdatedEvent.mnemonicCode
                }
                .also { this.materialEntryRepository.save(it) }
    }

    @EventHandler
    fun onEnabled(materialEnabledEvent: MaterialEnabledEvent) {
        this.materialEntryRepository.findOne(materialEnabledEvent.uid).invalidOperationIfNull().also { it.enabled = true }.also { this.materialEntryRepository.save(it) }
    }

    @EventHandler
    fun onDisabled(materialDisabledEvent: MaterialDisabledEvent) {
        this.materialEntryRepository.findOne(materialDisabledEvent.uid)
                .invalidOperationIfNull()
                .also { it.enabled = false }
                .also { this.materialEntryRepository.save(it) }

    }


}