package cn.apier.attendance.query.entry

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class ProcessingValue(@Id val uid: String, val code: String, var value: String)

@Entity
data class AttendanceInfoEntry(@Id var uid: String, var userId: String, var checkDate: String, var checkIn: Date, var checkOut: Date, var duration: Long = 0, var isLate: Boolean = false)