package cn.apier.attendance.query.entry

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class AttendanceUserEntry(@Id val uid: String, val name: String, val enabled: Boolean, val createdAt: Date, var mnemonic: String)