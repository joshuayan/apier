package cn.apier.standard.domain.model

import cn.apier.common.domain.model.BaseModel
import cn.apier.common.util.DateTimeUtil
import cn.apier.standard.domain.event.DistrictCreatedEvent
import cn.apier.standard.domain.event.DistrictUpdatedEvent
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate

/**
 * Created by yanjunhua on 2017/6/14.
 */
@Aggregate
class District : BaseModel {
    private constructor()
    constructor(uid: String, cityId: String, name: String,code:String,  description: String?) {
        AggregateLifecycle.apply(DistrictCreatedEvent(uid, cityId, name,code, DateTimeUtil.now(), description))
    }

    private var name: String = ""
    private var description: String? = null
    private var cityId: String = ""
    private var code:String=""

    fun update(cityId: String, name: String,code:String,  description: String?) {
        AggregateLifecycle.apply(DistrictUpdatedEvent(uid, cityId, name,code, description))
    }

    @EventSourcingHandler
    fun onUpdated(districtUpdatedEvent: DistrictUpdatedEvent) {
        this.description = districtUpdatedEvent.description
        this.cityId = districtUpdatedEvent.cityId
        this.name = districtUpdatedEvent.name
        this.code=districtUpdatedEvent.code
    }

    @EventSourcingHandler
    fun onCreation(districtCreatedEvent: DistrictCreatedEvent) {
        this.uid = districtCreatedEvent.uid
        this.name = districtCreatedEvent.name
        this.cityId = districtCreatedEvent.cityId
        this.description = districtCreatedEvent.description
        this.createdAt = districtCreatedEvent.createdAt
        this.code=districtCreatedEvent.code
    }
}