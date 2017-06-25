package cn.apier.mm.domain.event

import java.util.*

/**
 * Created by yanjunhua on 2017/6/23.
 */


data class PackageReceiptCreatedEvent(val uid: String, val deliveryManId: String, val billDate: Date, val createdAt: Date,val deleted:Boolean)

data class PackageReceiptDeletedEvent(val uid: String)

data class PackageReceiptItemAddedEvent(val uid: String, val materialId: String, val quantityDelivered: Int, val quantityRecalled: Int)

