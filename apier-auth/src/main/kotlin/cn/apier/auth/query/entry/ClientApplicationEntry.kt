package cn.apier.auth.query.entry

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

//@Entity
//data class ClientApplicationEntry(val enterpriseId: String = "", @Id val uid: String = "", val name: String = "", val appKey: String = "",
//                                  val appSecret: String = "", var deleted: Boolean = false, val createdAt: Date = DateTimeUtil.now())

@Entity
data class ClientApplicationEntry(val enterpriseId: String, @Id val uid: String, val name: String, val appKey: String,
                                  val appSecret: String, var deleted: Boolean, val createdAt: Date)