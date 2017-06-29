package cn.apier.mm.query.listener

import cn.apier.mm.domain.event.CategoryCreatedEvent
import cn.apier.mm.domain.event.CategoryDisabledEvent
import cn.apier.mm.domain.event.CategoryEnabledEvent
import cn.apier.mm.domain.event.CategoryUpdatedEvent
import cn.apier.mm.query.dao.CategoryEntryRepository
import cn.apier.mm.query.entry.CategoryEntry
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/21.
 */

@Component
class CategoryEntryListener {

    @Autowired
    lateinit var categoryEntryRepository: CategoryEntryRepository


    @EventHandler
    fun onCreation(categoryCreatedEvent: CategoryCreatedEvent) {
        CategoryEntry(categoryCreatedEvent.tenantId, categoryCreatedEvent.uid, categoryCreatedEvent.name,
                categoryCreatedEvent.enabled, categoryCreatedEvent.createdAt, categoryCreatedEvent.description).also {
            this.categoryEntryRepository.save(it)
        }
    }

    @EventHandler
    fun onUpdated(categoryUpdatedEvent: CategoryUpdatedEvent) =
            with(this.categoryEntryRepository) {
                findOne(categoryUpdatedEvent.uid)
                        .also { it.name = categoryUpdatedEvent.name;it.description = categoryUpdatedEvent.description }
                        .also { save(it) }
            }

    @EventHandler
    fun onEnabled(categoryEnabledEvent: CategoryEnabledEvent) = with(this.categoryEntryRepository) {
        findOne(categoryEnabledEvent.uid).also { it.enabled = true }.also { save(it) }
    }

    @EventHandler
    fun onDisabled(categoryDisabledEvent: CategoryDisabledEvent) = with(this.categoryEntryRepository) {
        findOne(categoryDisabledEvent.uid).also { it.enabled = false }.also { save(it) }
    }

}