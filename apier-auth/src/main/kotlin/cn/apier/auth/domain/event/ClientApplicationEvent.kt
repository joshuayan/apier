package cn.apier.auth.domain.event

import java.util.*


data class ClientApplicationCreatedEvent(val enterpriseId: String, val uid: String, val name: String, val appKey: String,
                                         val appSecret: String, val deleted: Boolean, val createdAt: Date)


