package cn.apier.standard.domain.event

import java.util.*

/**
 * Created by yanjunhua on 2017/6/15.
 */

data class CountryCreatedEvent(val id: String, val name: String,val code:String, val createdAt: Date, val description: String?)


data class CountryUpdatedEvent(val id: String, val name: String,val code:String, val description: String? = null)