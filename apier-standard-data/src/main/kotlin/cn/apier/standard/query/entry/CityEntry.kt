package cn.apier.standard.query.entry

import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by yanjunhua on 2017/6/14.
 */
@Entity
data class CityEntry(@Id val id: String="",val provinceId:String="", val name: String="", val description: String?="") {
}