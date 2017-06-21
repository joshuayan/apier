package cn.apier.standard.application.command

import java.util.*

/**
 * Created by yanjunhua on 2017/6/15.
 */


data class CreateCountryCommand(val id: String, val name: String,val code:String, val createdAt: Date, val description: String? = null)

data class UpdateCountryCommand(val id: String, val name: String,val code:String, val description: String?)