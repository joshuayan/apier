package cn.apier.attendance.query.dao

import cn.apier.attendance.query.entry.AttendanceInfoEntry
import cn.apier.attendance.query.entry.AttendanceRecordEntry
import cn.apier.attendance.query.entry.ProcessingValue
import cn.apier.attendance.query.entry.AttendanceUserEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface AttendanceUserEntryRepository : JpaRepository<AttendanceUserEntry, String> {
    fun findByMnemonicIsNull(): List<AttendanceUserEntry>
    fun findByMnemonicContaining(mnemonic: String): List<AttendanceUserEntry>
}


@Repository
interface AttendanceRecordEntryRepository : JpaRepository<AttendanceRecordEntry, String> {
    fun findByCheckDateGreaterThan(checkDate: String): List<AttendanceRecordEntry>
}


@Repository
interface ProcessingValueRepository : JpaRepository<ProcessingValue, String> {
    fun findByCode(code: String): ProcessingValue?
}


@Repository
interface AttendanceInfoEntryRepository : JpaRepository<AttendanceInfoEntry, String> {

    fun findByUserIdAndCheckDateBetween(userId: String, startDate: LocalDate, endDate: LocalDate): List<AttendanceInfoEntry>
    fun findByUserIdAndCheckDate(userId: String, checkDate: String): AttendanceInfoEntry?
}