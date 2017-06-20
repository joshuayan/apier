package cn.apier.standard.domain.event

import java.util.*

/**
 * Created by yanjunhua on 2017/6/16.
 */


data class CityCreatedEvent(val uid: String, val provinceId: String, val name: String,val createdAt:Date, val description: String?)


data class CityUpdatedEvent(val uid: String, val provinceId: String, val name: String, val description: String?)