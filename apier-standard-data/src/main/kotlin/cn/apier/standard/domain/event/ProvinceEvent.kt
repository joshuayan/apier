package cn.apier.standard.domain.event

import java.util.*

/**
 * Created by yanjunhua on 2017/6/16.
 */

data class ProvinceCreatedEvent(val uid: String, val countryId: String, val name: String,val code:String,val createdAt:Date, val description: String?)

data class ProvinceUpdatedEvent(val uid: String, val countryId: String, val name: String,val code:String, val description: String?)