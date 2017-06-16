package cn.apier.standard.domain.model

import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by yanjunhua on 2017/6/7.
 */
class City(private var id: String, private  var provinceId: String,private var name: String,private  var description: String = "") {

//    var name: String = name
//        private set
//
//    var provinceId: String = provinceId
//        private set
//
//    var description: String = description
//        private set

    fun update(name: String, provinceId: String? = null, description: String? = null) {
        this.name = name
        this.description = description ?: this.description
        this.provinceId = provinceId ?: this.provinceId
    }

}