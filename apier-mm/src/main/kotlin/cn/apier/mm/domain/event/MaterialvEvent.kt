package cn.apier.mm.domain.event

import java.util.*

/**
 * Created by yanjunhua on 2017/6/22.
 */

data class MaterialCreatedEvent(val uid: String, val code: String, val name: String,val categoryId:String,var mnemonicCode:String,var enabled:Boolean, val createdAt: Date, val description: String?)

data class MaterialUpdatedEvent(val uid: String, val code: String, val name: String,val categoryId:String,var mnemonicCode:String, val description: String?)
data class MaterialEnabledEvent(val uid: String, val code: String)
data class MaterialDisabledEvent(val uid: String, val code: String)
