package cn.apier.standard.domain.event

import java.util.*

/**
 * Created by yanjunhua on 2017/6/16.
 */


data class DistrictCreatedEvent(val uid: String, val cityId: String, val name: String, val createdAt: Date, val description: String?)


data class DistrictUpdatedEvent(val uid: String, val cityId: String, val name: String, val description: String?)