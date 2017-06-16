package cn.apier.standard.query.listener

import cn.apier.standard.domain.event.CountryCreatedEvent
import cn.apier.standard.domain.event.CountryUpdatedEvent
import cn.apier.standard.query.dao.CountryEntryRepository
import cn.apier.standard.query.entry.CountryEntry
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * Created by yanjunhua on 2017/6/15.
 */

@Component
class CountryListener {

    @Autowired
    lateinit var countryEntryRepository: CountryEntryRepository

    @EventHandler
    fun onCreation(createdEvent: CountryCreatedEvent) {
        Mono.just<CountryEntry>(CountryEntry(createdEvent.id, createdEvent.name, createdEvent.createdAt, createdEvent.description))
                .subscribe { this.countryEntryRepository.save(it) }
    }


    @EventHandler
    fun onUpdated(updatedEvent: CountryUpdatedEvent) {

        Mono.just<CountryEntry>(this.countryEntryRepository.getOne(updatedEvent.id)).subscribe { it.name = updatedEvent.name;it.description = updatedEvent.description;this.countryEntryRepository.save(it) }
    }
}