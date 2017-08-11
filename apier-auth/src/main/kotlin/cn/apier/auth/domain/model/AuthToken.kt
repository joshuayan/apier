package cn.apier.auth.domain.model

import cn.apier.auth.domain.event.AuthTokenCreatedEvent
import cn.apier.common.domain.model.EnterpriseDeletableBaseModel
import cn.apier.common.util.DateTimeUtil
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.util.*


@Aggregate
class AuthToken : EnterpriseDeletableBaseModel {

    private var clientUid: String = ""
    private var code: String = ""
    private var expiredAt: Date = DateTimeUtil.now()


    private constructor()
    constructor(enterpriseId: String, uid: String, code: String, clientUid: String, appKey: String, expiredAt: Date) {
        AggregateLifecycle.apply(AuthTokenCreatedEvent(enterpriseId, uid, code, clientUid, appKey, expiredAt, DateTimeUtil.now(), false))
    }


    @EventSourcingHandler
    private fun onCreated(authTokenCreatedEvent: AuthTokenCreatedEvent) {
        this.uid = authTokenCreatedEvent.uid
        this.clientUid = authTokenCreatedEvent.clientUid
        this.expiredAt = authTokenCreatedEvent.expiredAt
        this.enterpriseId = authTokenCreatedEvent.enterpriseId
        this.code = authTokenCreatedEvent.code
        this.deleted = authTokenCreatedEvent.deleted
    }
}