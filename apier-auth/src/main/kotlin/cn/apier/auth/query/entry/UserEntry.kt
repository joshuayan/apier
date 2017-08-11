package cn.apier.auth.query.entry

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
//data class UserEntry(@Id var uid: String = "", var mobile: String = "", var password: String = "", var enabled: Boolean = true, var createdAt: Date = DateTimeUtil.now())
data class UserEntry(@Id var uid: String , var mobile: String, var password: String , var enabled: Boolean, var createdAt: Date )
