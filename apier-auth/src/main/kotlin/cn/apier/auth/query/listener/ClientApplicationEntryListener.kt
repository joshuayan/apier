package cn.apier.auth.query.listener

import cn.apier.auth.domain.event.ClientApplicationCreatedEvent
import cn.apier.auth.query.entry.ClientApplicationEntry
import cn.apier.auth.query.repository.ClientApplicationEntryRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ClientApplicationEntryListener {

    @Autowired
    private lateinit var clientApplicationEntryRepository: ClientApplicationEntryRepository


    @EventHandler
    fun onCreated(clientApplicationCreatedEvent: ClientApplicationCreatedEvent) {
        ClientApplicationEntry(clientApplicationCreatedEvent.enterpriseId, clientApplicationCreatedEvent.uid, clientApplicationCreatedEvent.name
                , clientApplicationCreatedEvent.appKey, clientApplicationCreatedEvent.appSecret, clientApplicationCreatedEvent.deleted
                , clientApplicationCreatedEvent.createdAt).also { this.clientApplicationEntryRepository.save(it) }
    }

}