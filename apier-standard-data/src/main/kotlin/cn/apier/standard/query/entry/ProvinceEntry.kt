package cn.apier.standard.query.entry

import cn.apier.common.query.entry.BaseEntry
import cn.apier.common.util.DateTimeUtil
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by yanjunhua on 2017/6/14.
 */

@Entity
data class ProvinceEntry(@Id val id: String = "",val countryId:String="", val name: String = "", val createdAt: Date = DateTimeUtil.now(), val description: String? = null) {
}