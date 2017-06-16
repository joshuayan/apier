package cn.apier.standard.domain.model

import cn.apier.common.util.DateTimeUtil
import cn.apier.standard.query.entry.ProvinceEntry
import java.util.*

/**
 * Created by yanjunhua on 2017/6/7.
 */
class Province(val id: String, val countryId: String, name: String, createdAt: Date = DateTimeUtil.now(), description: String? = "") {

    var name: String = name
        private set
    var description: String? = description
        private set
    val createdAt: Date = createdAt

    fun update(name: String, description: String? = null) {
        this.name = name
        this.description = description ?: this.description
    }
}