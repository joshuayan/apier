package cn.apier.auth.query.entry

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class AuthTokenEntry(val enterpriseId: String, @Id val uid: String, val code: String,val appKey:String, val expiredAt: Date, val createdAt: Date, val deleted: Boolean)