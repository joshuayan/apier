package cn.apier.standard.application.command

/**
 * Created by yanjunhua on 2017/6/20.
 */
data class CreateDistrictCommand(val uid: String,val cityId:String, val name: String, val code:String,val description: String?)

data class UpdateDistrictCommand(val uid: String,val cityId:String, val name: String,val code:String, val description: String?)

