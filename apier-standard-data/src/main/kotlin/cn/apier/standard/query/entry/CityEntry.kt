package cn.apier.standard.query.entry

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by yanjunhua on 2017/6/14.
 */
@Entity
data class CityEntry(@Id val id: String="",var provinceId:String="", var name: String="",val createdAt:Date, var description: String?="")