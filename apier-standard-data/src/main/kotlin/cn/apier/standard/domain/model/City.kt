package cn.apier.standard.domain.model

import cn.apier.common.domain.model.BaseModel
import cn.apier.common.util.DateTimeUtil
import cn.apier.standard.domain.event.CityCreatedEvent
import cn.apier.standard.domain.event.CityUpdatedEvent
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate

/**
 * Created by yanjunhua on 2017/6/7.
 */
@Aggregate
class City : BaseModel {

    private constructor()
    constructor(uid: String, provinceId: String, name: String, code:String,  description: String?) {
        AggregateLifecycle.apply(CityCreatedEvent(uid, provinceId, name,code, DateTimeUtil.now(), description))
    }

    private var name: String = ""
    private var provinceId: String = ""
    private var description: String? = null
    private var code:String=""

    fun update(name: String, provinceId: String,code:String,  description: String?) {
        AggregateLifecycle.apply(CityUpdatedEvent(uid, provinceId, name,code, description))
    }


    @EventSourcingHandler
    fun onCreation(cityCreatedEvent: CityCreatedEvent) {
        this.uid = cityCreatedEvent.uid
        this.name = cityCreatedEvent.name
        this.provinceId = cityCreatedEvent.provinceId
        this.description = cityCreatedEvent.description
        this.createdAt = cityCreatedEvent.createdAt
        this.code=cityCreatedEvent.code
    }

    @EventSourcingHandler
    fun onUpdated(cityUpdatedEvent: CityUpdatedEvent) {
        this.name = cityUpdatedEvent.name
        this.provinceId = cityUpdatedEvent.provinceId
        this.description = cityUpdatedEvent.description
        this.code=cityUpdatedEvent.code
    }

}