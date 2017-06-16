package cn.apier.common.query.entry

import cn.apier.common.util.DateTimeUtil
import java.util.*
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Created by yanjunhua on 2017/6/15.
 */
@MappedSuperclass
abstract class BaseEntry(@Id val id: String = "", val createdAt: Date = DateTimeUtil.now()) {
}