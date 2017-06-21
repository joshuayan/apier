package cn.apier.standard.application.command

/**
 * Created by yanjunhua on 2017/6/20.
 */

data class CreateCityCommand(val uid: String,val provinceId:String, val name: String,val code:String, val description: String?)

data class UpdateCityCommand(val uid: String,val provinceId:String, val name: String,val code:String, val description: String?)

