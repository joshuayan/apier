package cn.apier.attendance.query.entry

import cn.apier.common.util.DateTimeUtil
import java.util.*
import javax.persistence.*


@Entity
class AttendanceRecordEntry {

    var userId: String = ""
    var checkTime: Date = DateTimeUtil.now()
    var checkDate: String = ""
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var seqId: Long = 0
    var name: String = ""
}