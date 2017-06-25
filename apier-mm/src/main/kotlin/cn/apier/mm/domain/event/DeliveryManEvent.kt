package cn.apier.mm.domain.event

import java.util.*

/**
 * Created by yanjunhua on 2017/6/22.
 */

data class DeliveryManCreatedEvent(val uid: String, val name: String, val mobile: String, val mnemonicCode: String, val enabled: Boolean, val createdAt: Date, val description: String?)

data class DeliveryManUpdatedEvent(val uid: String, val name: String, val mobile: String, val mnemonicCode: String, val description: String?)

data class DeliveryManEnabledEvent(val uid: String, val name: String)
data class DeliveryManDisabledEvent(val uid: String, val name: String)