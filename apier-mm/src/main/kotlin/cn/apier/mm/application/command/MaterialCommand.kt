package cn.apier.mm.application.command

/**
 * Created by yanjunhua on 2017/6/22.
 */


data class CreateMaterialCommand(val uid: String,val tenantId:String, val name: String, val code: String, val categoryId: String, val enabled: Boolean,val businessScope: String, val description: String?)

data class UpdateMaterialCommand(val uid: String, val name: String, val categoryId: String, val description: String?)

data class EnableMaterialCommand(val uid: String)

data class DisableMaterialCommand(val uid: String)