package cn.apier.standard.query.entry

import cn.apier.common.util.DateTimeUtil
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by yanjunhua on 2017/6/14.
 */
@Entity
data class CityEntry(@Id val uid: String="", var provinceId:String="", var name: String="",var code:String="", val createdAt:Date=DateTimeUtil.now(), var description: String?="")