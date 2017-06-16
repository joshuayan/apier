package cn.apier.common.domain.model

import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

/**
 * Created by yanjunhua on 2017/6/15.
 */

abstract class BaseModel {
    @AggregateIdentifier
    lateinit protected var uid: String
    lateinit protected var createdAt: Date
}