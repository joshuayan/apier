package cn.apier.standard.domain.model

import javax.persistence.Entity

/**
 * Created by yanjunhua on 2017/6/14.
 */
class District(val id: String, name: String, description: String?) {
    var name: String = name
        private set
    var description: String? = description
        private set

    fun update(name: String, description: String?) {
        this.name = name
        this.description = description ?: this.description
    }
}