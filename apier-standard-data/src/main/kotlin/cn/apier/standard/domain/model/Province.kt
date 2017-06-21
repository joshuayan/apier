package cn.apier.standard.domain.model

import cn.apier.common.domain.model.BaseModel
import cn.apier.common.util.DateTimeUtil
import cn.apier.standard.domain.event.ProvinceCreatedEvent
import cn.apier.standard.domain.event.ProvinceUpdatedEvent
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate

/**
 * Created by yanjunhua on 2017/6/7.
 */
@Aggregate
class Province : BaseModel {

    private constructor()
    constructor(uid: String, countryId: String, name: String, code: String, description: String?) : this() {
        AggregateLifecycle.apply(ProvinceCreatedEvent(uid, countryId, name, code, DateTimeUtil.now(), description))
    }

    private var name: String = ""
    private var countryId: String = ""
    private var description: String? = null
    private var code: String = ""

    fun update(name: String, countryId: String, code: String, description: String? = null) {
        AggregateLifecycle.apply(ProvinceUpdatedEvent(this.uid, countryId, name, code, description))
    }


    @EventSourcingHandler
    fun onCreation(provinceCreatedEvent: ProvinceCreatedEvent) {
        this.uid = provinceCreatedEvent.uid
        this.name = provinceCreatedEvent.name
        this.countryId = provinceCreatedEvent.countryId
        this.description = provinceCreatedEvent.description
        this.createdAt = provinceCreatedEvent.createdAt
        this.code = provinceCreatedEvent.code
    }


    @EventSourcingHandler
    fun onUpdated(provinceUpdatedEvent: ProvinceUpdatedEvent) {
        this.countryId = provinceUpdatedEvent.countryId
        this.name = provinceUpdatedEvent.name
        this.description = provinceUpdatedEvent.description
        this.code = provinceUpdatedEvent.code
    }
}