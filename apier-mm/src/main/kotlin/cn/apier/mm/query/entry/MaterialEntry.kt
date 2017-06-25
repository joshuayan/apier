package cn.apier.mm.query.entry

import cn.apier.common.util.DateTimeUtil
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by yanjunhua on 2017/6/22.
 */
@Entity
data class MaterialEntry(@Id val uid: String = "", var code: String = "", var name: String = "", var categoryId: String = "", var mnemonicCode: String = "", var enabled: Boolean = false, var createdAt: Date = DateTimeUtil.now(), var description: String? = null)