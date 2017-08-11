package cn.apier.mm.application.command

/**
 * Created by yanjunhua on 2017/6/21.
 */


data class CreateCategoryCommand(val tenantId:String,val uid: String, val name: String, val enabled: Boolean, val description: String?)

data class UpdateCategoryCommand(val uid: String, val name: String, val description: String?)

data class EnableCategoryCommand(val uid: String)

data class DisableCategoryCommand(val uid: String)