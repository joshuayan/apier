package cn.apier.auth.domain.event

import java.util.*

data class UserCreatedEvent(val uid: String, val mobile: String, val password: String, val createdAt: Date, val enabled: Boolean)

data class UserPasswordUpdatedEvent(val uid: String, val password: String)
