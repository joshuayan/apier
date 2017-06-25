package cn.apier.mm.domain.event

import java.util.*

/**
 * Created by yanjunhua on 2017/6/21.
 */

data class CategoryCreatedEvent(val uid: String, val name: String,val enabled:Boolean, val createdAt: Date, val description: String?)

data class CategoryUpdatedEvent(val uid: String, val name: String, val description: String?)
data class CategoryEnabledEvent(val uid: String, val name: String)
data class CategoryDisabledEvent(val uid: String, val name: String)
