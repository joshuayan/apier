package cn.apier.mm.application.command

/**
 * Created by yanjunhua on 2017/6/22.
 */


data class CreateDeliveryManCommand(val uid: String, val name: String, val mobile: String, val enabled: Boolean, val description: String?)

data class UpdateDeliveryManCommand(val uid: String, val name: String, val mobile: String, val description: String?)

data class EnableDeliveryManCommand(val uid: String)

data class DisableDeliveryManCommand(val uid: String)