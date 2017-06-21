package cn.apier.standard.domain.model

import cn.apier.common.domain.model.BaseModel
import cn.apier.common.util.DateTimeUtil
import cn.apier.standard.domain.event.CountryCreatedEvent
import cn.apier.standard.domain.event.CountryUpdatedEvent
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate

/**
 * Created by yanjunhua on 2017/6/6.
 */
@Aggregate
class Country : BaseModel {

    private var name: String = ""
    private var description: String? = null
    private var code:String=""

    private constructor()

    constructor(uid: String, name: String,code:String,  description: String?) : this() {
        AggregateLifecycle.apply(CountryCreatedEvent(uid, name,code, DateTimeUtil.now(), description))
    }


    fun update(uid: String, name: String,code:String,  description: String?) {
        AggregateLifecycle.apply(CountryUpdatedEvent(uid, name,code, description))
    }

    @EventSourcingHandler
    fun onCreation(createdEvent: CountryCreatedEvent) {
        this.uid = createdEvent.id
        this.name = createdEvent.name
        this.createdAt = createdEvent.createdAt
        this.description = createdEvent.description
        this.code=createdEvent.code
    }

    @EventSourcingHandler
    fun onUpdated(countryUpdatedEvent: CountryUpdatedEvent) {
        this.name = countryUpdatedEvent.name
        this.description = countryUpdatedEvent.description ?: this.description
        this.code=countryUpdatedEvent.code
    }
}