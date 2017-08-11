package cn.apier.auth.domain.model

import cn.apier.auth.domain.event.ClientApplicationCreatedEvent
import cn.apier.common.domain.model.EnterpriseDeletableBaseModel
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class ClientApplication : EnterpriseDeletableBaseModel {

    private var name: String = ""
    private var appKey: String = ""
    private var appSecret: String = ""

    private constructor()
    constructor(enterpriseId: String, uid: String, name: String, appKey: String, appSecret: String, deleted: Boolean, createdAt: Date) {
        AggregateLifecycle.apply(ClientApplicationCreatedEvent(enterpriseId, uid, name, appKey, appSecret, deleted, createdAt))
    }


    @EventSourcingHandler
    private fun onCreated(clientApplicationCreatedEvent: ClientApplicationCreatedEvent) {
        this.enterpriseId = clientApplicationCreatedEvent.enterpriseId
        this.uid = clientApplicationCreatedEvent.uid
        this.deleted = clientApplicationCreatedEvent.deleted
        this.appKey = clientApplicationCreatedEvent.appKey
        this.appSecret = clientApplicationCreatedEvent.appSecret
        this.createdAt = clientApplicationCreatedEvent.createdAt
        this.name = clientApplicationCreatedEvent.name
    }
}