package cn.apier.permission.domain.event

import java.util.*


data class ResourceCreated(val uid: String, val name: String, val remark: String, val createdAt: Date)

data class ResourceActionAdded(val uid: String, val resourceId: String, val actionId: String, val createdAt: Date)

data class ActionCreated(val uid: String, val name: String, val remark: String, val createdAt: Date)