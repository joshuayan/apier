package cn.apier.mm.query.listener

import cn.apier.mm.domain.event.CategoryCreatedEvent
import cn.apier.mm.query.dao.CategoryEntryRepository
import cn.apier.mm.query.entry.CategoryEntry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by yanjunhua on 2017/6/21.
 */

@Component
class CategoryEntryListener {

    @Autowired
    lateinit var categoryEntryRepository: CategoryEntryRepository


    fun onCreation(categoryCreatedEvent: CategoryCreatedEvent) {
        this.categoryEntryRepository.save(CategoryEntry(categoryCreatedEvent.uid, categoryCreatedEvent.name,
                categoryCreatedEvent.enabled, categoryCreatedEvent.createdAt, categoryCreatedEvent.description))
    }
}