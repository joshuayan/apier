package cn.apier.attendance.query.entry

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id


@Entity
data class AttendanceRecordEntry(@Id val uid: String, val userId: String, val checkTime: Date, val checkDate: String)