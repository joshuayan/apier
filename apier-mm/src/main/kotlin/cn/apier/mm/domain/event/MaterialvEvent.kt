package cn.apier.mm.domain.event

import cn.apier.mm.domain.model.BusinessScope
import java.util.*

/**
 * Created by yanjunhua on 2017/6/22.
 */

data class MaterialCreatedEvent(val tenantId: String, val uid: String, val code: String, val name: String, val categoryId: String, var mnemonicCode: String, val enabled: Boolean, val bussinessScope: BusinessScope, val createdAt: Date, val description: String?)

data class MaterialUpdatedEvent(val uid: String, val code: String, val name: String, val categoryId: String, var mnemonicCode: String, val description: String?)
data class MaterialEnabledEvent(val uid: String, val code: String)
data class MaterialDisabledEvent(val uid: String, val code: String)
