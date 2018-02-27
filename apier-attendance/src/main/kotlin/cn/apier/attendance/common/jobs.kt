package cn.apier.attendance.common

import cn.apier.attendance.query.dao.AttendanceInfoEntryRepository
import cn.apier.attendance.query.dao.AttendanceRecordEntryRepository
import cn.apier.attendance.query.dao.AttendanceUserEntryRepository
import cn.apier.attendance.query.dao.ProcessingValueRepository
import cn.apier.attendance.query.entry.AttendanceInfoEntry
import cn.apier.attendance.query.entry.AttendanceUserEntry
import cn.apier.attendance.query.entry.ProcessingValue
import cn.apier.common.util.DateTimeUtil
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalTime
import java.util.*


@Component
class BuildDailyAttendanceJob {

    @Autowired
    private lateinit var attendanceRecordEntryRepository: AttendanceRecordEntryRepository
    @Autowired
    private lateinit var attendanceInfoEntryRepository: AttendanceInfoEntryRepository

    @Autowired
    private lateinit var processingValueRepository: ProcessingValueRepository

    @Autowired
    private lateinit var userEntryRepository: AttendanceUserEntryRepository

    companion object {
        val CODE_MAX_ATTENDANCE_INFO_SEQ_ID = "MAX_ATTENDANCE_INFO_SEQ_ID"
        //        val CODE_MAX_ATTENDANCE_INFO_DATE = "MAX_ATTENDANCE_INFO_DATE"
        val LOGGER = LoggerFactory.getLogger(BuildDailyAttendanceJob::class.java)
    }

    fun doJob() {

        val userMap: MutableMap<String, AttendanceUserEntry> = mutableMapOf()
        runBlocking {
            launch {
                userEntryRepository.findAll().parallelStream().forEach { userMap.put(it.uid, it) }
            }
        }

        var maxSeqId = 0L

        val maxProcessingValue = processingValueRepository.findByCode(CODE_MAX_ATTENDANCE_INFO_SEQ_ID)

        val currentPv = Optional.ofNullable(maxProcessingValue).orElseGet { ProcessingValue(UUID.randomUUID().toString(), CODE_MAX_ATTENDANCE_INFO_SEQ_ID, "0") }
        maxSeqId = currentPv.value.toLong()

        val records = attendanceRecordEntryRepository.findByCheckDateGreaterThan(currentPv.value)

        val infoList = mutableMapOf<String/*date_userId*/, AttendanceInfoEntry>()



        records.stream().forEach {
            val userId = it.userId
            var name = userMap[userId]?.name ?: "unknown"
            val checkDate = it.checkDate
            val key = "${checkDate}_$userId"

            if (!infoList.containsKey(key)) {
                this.attendanceInfoEntryRepository.findByUserIdAndCheckDate(userId, checkDate)?.also { infoList.put(key, it) }
            }


            val infoEntry = infoList.getOrPut(key, { AttendanceInfoEntry(UUID.randomUUID().toString(), userId, name, checkDate, it.checkTime, it.checkTime) })
            infoEntry.checkIn = minOf(it.checkTime, infoEntry.checkIn)
            infoEntry.checkOut = maxOf(it.checkTime, infoEntry.checkOut)

            val duration = (infoEntry.checkOut.time - infoEntry.checkIn.time) / 3600000
            infoEntry.duration = duration
            infoEntry.isLate = checkLate(infoEntry.checkIn)

            maxSeqId = maxOf(maxSeqId, it.seqId)
        }

        currentPv.value = maxSeqId.toString()
        attendanceInfoEntryRepository.saveAll(infoList.values)
        processingValueRepository.save(currentPv)
        LOGGER.debug("Build Daily Attendance DONE")

    }

    private fun checkLate(checkIn: Date): Boolean {

        val checkInStr = DateTimeUtil.formatDate(checkIn, "HH:mm:ss")
        val checkInTime = LocalTime.parse(checkInStr)

        val lastCheckInTime = LocalTime.parse("09:30:00")

        return lastCheckInTime.isBefore(checkInTime)

    }
}