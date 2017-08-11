package cn.apier.auth.domain.event

import java.util.*


data class AuthTokenCreatedEvent(val enterpriseId: String, val uid: String,val code:String, val clientUid: String,val appKey:String, val expiredAt:Date, val createdAt: Date, val deleted: Boolean)