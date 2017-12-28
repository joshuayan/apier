package cn.apier.attendance.application.api

import cn.apier.attendance.application.service.UserFixService
import cn.apier.attendance.common.BuildDailyAttendanceJob
import cn.apier.attendance.import.AttendanceImporter
import cn.apier.attendance.query.dao.AttendanceInfoEntryRepository
import cn.apier.attendance.query.dao.AttendanceInfoMapper
import cn.apier.attendance.query.dao.AttendanceUserEntryRepository
import cn.apier.attendance.query.entry.AttendanceInfoEntry
import cn.apier.attendance.query.entry.AttendanceUserEntry
import cn.apier.common.api.Result
import cn.apier.common.extension.execute
import cn.apier.common.extension.query
import cn.apier.common.extension.queryWithPage
import cn.apier.common.util.ExecuteTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate


@CrossOrigin(origins = ["http://localhost:8080"])
@RequestMapping("/attendance")
@RestController
class AttendanceApi {

    @Autowired
    private lateinit var attendanceImporter: AttendanceImporter
    @Autowired
    private lateinit var buildAttendanceJob: BuildDailyAttendanceJob

    @Autowired
    private lateinit var userFixService: UserFixService

    @Autowired
    private lateinit var userEntryRepository: AttendanceUserEntryRepository

    @Autowired
    private lateinit var attendanceInfoRepository: AttendanceInfoEntryRepository

    @Autowired
    private lateinit var attendanceInfoMapper: AttendanceInfoMapper

    @GetMapping("/import/user")
    fun importUser() {
        attendanceImporter.importUser()
    }

    @GetMapping("/import/attendance")
    fun importAttendanceRecord() {
        attendanceImporter.importAttendanceRecord()
    }

    @GetMapping("/job/build")
    fun buildAttendanceInfo() {
        buildAttendanceJob.doJob()
    }

    @GetMapping("/fix/mnemonic")
    fun fixUserMnemonic(): Result<Any> = execute {
        this.userFixService.fixMnemonic()
    }


    @GetMapping("/user/queryByMnemonic")
    fun queryUserByMnemonic(mnemonic: String): Result<List<AttendanceUserEntry>> = query { this.userEntryRepository.findByMnemonicContaining(mnemonic) }


    @GetMapping("/query")
    fun queryAttendanceInfo(@DateTimeFormat(style = "yyyy-MM-dd") startDate: String?, @DateTimeFormat(style = "yyyy-MM-dd") endDate: String?, userId: String?): Result<List<AttendanceInfoEntry>> =
            queryWithPage { this.attendanceInfoMapper.findByUserIdAndCheckDate(userId, startDate, endDate) }


}

