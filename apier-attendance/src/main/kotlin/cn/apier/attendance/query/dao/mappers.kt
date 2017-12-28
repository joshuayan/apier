package cn.apier.attendance.query.dao

import cn.apier.attendance.query.entry.AttendanceInfoEntry
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import java.time.LocalDate

@Mapper
interface AttendanceInfoMapper {

    fun findByUserIdAndCheckDate(@Param("userId") userId: String?, @Param("startDate") startDate: String?, @Param("endDate") endDate: String?):List<AttendanceInfoEntry>
}