package cn.apier.common.domain.model

import cn.apier.common.util.DateTimeUtil
import org.axonframework.commandhandling.model.AggregateIdentifier
import java.util.*

/**
 * Created by yanjunhua on 2017/6/15.
 */

abstract class BaseModel {
//    protected constructor()

    @AggregateIdentifier
     lateinit var uid: String
    protected var createdAt: Date = DateTimeUtil.now()
}

abstract class EnabledBaseModel : BaseModel() {
    protected var enabled: Boolean = true
}

abstract class DeletableBaseModel : BaseModel() {
    protected var deleted: Boolean = false
}

abstract class EnterpriseEnabledBaseModel : EnabledBaseModel() {
    protected var enterpriseId: String = ""
}


abstract class EnterpriseDeletableBaseModel : DeletableBaseModel() {
    protected var enterpriseId: String = ""
}
