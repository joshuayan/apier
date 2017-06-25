package cn.apier.mm.domain.model

import cn.apier.common.domain.model.DeletableBaseModel
import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.ExecuteTool
import cn.apier.common.util.UUIDUtil
import cn.apier.mm.domain.event.PackageReceiptCreatedEvent
import cn.apier.mm.domain.event.PackageReceiptItemAddedEvent
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import java.util.*

/**
 * Created by yanjunhua on 2017/6/23.
 */
class PackageReceipt : DeletableBaseModel {
    private var deliveryManId: String = ""
    private var billDate: Date = DateTimeUtil.now()
    private var items: List<PackageReceiptItem> = mutableListOf()


    private constructor()

    constructor(uid: String, deliveryManId: String, billDate: Date, createdAt: Date) {
        AggregateLifecycle.apply(PackageReceiptCreatedEvent(uid, deliveryManId, billDate, createdAt, false))
    }


    fun addItem(materialId: String, quantityDelivered: Int, quantityRecalled: Int) {
        ExecuteTool.invalidOperationIf { this.items.any { it.materialId.equals(materialId) } }

        AggregateLifecycle.apply(PackageReceiptItemAddedEvent(UUIDUtil.commonUUID(), materialId, quantityDelivered, quantityRecalled))
    }

    @EventSourcingHandler
    private fun onItemAdded(packageReceiptItemAddedEvent: PackageReceiptItemAddedEvent) {
        PackageReceiptItem(packageReceiptItemAddedEvent.uid, packageReceiptItemAddedEvent.materialId, packageReceiptItemAddedEvent.quantityDelivered, packageReceiptItemAddedEvent.quantityRecalled)
                .also { this.items.plus(it) }
    }


    @EventSourcingHandler
    private fun onCreation(packageReceiptCreatedEvent: PackageReceiptCreatedEvent) {
        this.uid = packageReceiptCreatedEvent.uid
        this.deliveryManId = packageReceiptCreatedEvent.deliveryManId
        this.billDate = packageReceiptCreatedEvent.billDate
        this.createdAt = packageReceiptCreatedEvent.createdAt
        this.deleted = packageReceiptCreatedEvent.deleted
    }

}


data class PackageReceiptItem(val uid: String, val materialId: String, var quantityDelivered: Int, var quantityRecalled: Int)