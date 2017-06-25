package cn.apier.mm.query.entry

import cn.apier.common.util.DateTimeUtil
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by yanjunhua on 2017/6/22.
 */

@Entity
data class DeliveryManEntry(@Id val uid: String = "", var name: String = "", var mobile: String = "", var mnemonicCode: String = "", var enabled: Boolean = true, var createdAt: Date = DateTimeUtil.now(), var description: String? = "")