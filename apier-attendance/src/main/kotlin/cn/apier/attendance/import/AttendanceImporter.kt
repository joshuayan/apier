package cn.apier.attendance.import

import cn.apier.attendance.query.dao.AttendanceRecordEntryRepository
import cn.apier.attendance.query.dao.AttendanceUserEntryRepository
import cn.apier.attendance.query.dao.ProcessingValueRepository
import cn.apier.attendance.query.entry.AttendanceRecordEntry
import cn.apier.attendance.query.entry.AttendanceUserEntry
import cn.apier.attendance.query.entry.ProcessingValue
import cn.apier.common.VertxContext
import cn.apier.common.util.DateTimeUtil
import com.github.stuxuhai.jpinyin.PinyinHelper
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import kotlin.math.max

@Component
class AttendanceImporter {

    @Autowired
    private lateinit var attendanceUserEntryRepository: AttendanceUserEntryRepository
    @Autowired
    private lateinit var attendanceRecordEntryRepository: AttendanceRecordEntryRepository
    @Autowired
    private lateinit var processingValueRepository: ProcessingValueRepository

    companion object {
        val CODE_MAX_ATTENDANCE_LAST_CODE = "MAX_ATTENDANCE_LAST_CODE"
        val CODE_MAX_CHECK_TIME = "MAX_CHECK_TIME"
        val LOGGER = LoggerFactory.getLogger(AttendanceImporter::class.java)
    }

    fun importUser() {

        runBlocking {
            val client = jdbcClient()

            val userJob = launch(CommonPool) {

                client.getConnection { connRes ->
                    if (connRes.succeeded()) {
                        val conn = connRes.result()
                        conn.query("select * from dbo.hr_userinfo ", { res ->

                            val userList = mutableListOf<AttendanceUserEntry>()
                            if (res.succeeded()) {
                                val resultSet = res.result()
                                LOGGER.debug("user count:${resultSet.numRows}")

                                resultSet.rows.forEach { row1 ->
                                    val userId = "${row1.getLong("UserId")}"
                                    val userName = row1.getString("ChineseNAME")
                                    val enabled = row1.getBoolean("Enable", false)
                                    val createdAt = row1.getInstant("Reg_bdDate", Instant.now())

                                    val mnemonic = PinyinHelper.getShortPinyin(userName)
                                    val user = AttendanceUserEntry(userId, userName, enabled, Date(createdAt.toEpochMilli()), mnemonic)
                                    userList.add(user)
                                    LOGGER.debug("user:$user")
                                }
                                attendanceUserEntryRepository.save(userList)
                                client.close()
                            }
                        })
                    } else {
                        LOGGER.warn("can not get connection")
                    }
                }
            }
            userJob.join()
        }
    }

    private fun jdbcClient(): JDBCClient {
        val config = json {
            obj(
                    "url" to "jdbc:sqlserver://192.168.0.201:1433;DatabaseName=CacheeCard",
                    "driver_class" to "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                    "user" to "sa",
                    "password" to "12345678",
                    "max_pool_size" to 3
            )
        }
        return JDBCClient.createShared(VertxContext.vertx(), config, "attendance_pool")
    }

    fun importAttendanceRecord() {


        runBlocking {
            val client = jdbcClient()
            val tmpMax = processingValueRepository.findByCode(CODE_MAX_CHECK_TIME)
            val tmpLastCode = processingValueRepository.findByCode(CODE_MAX_ATTENDANCE_LAST_CODE)
            val opLastCode = Optional.ofNullable(tmpLastCode).orElseGet { ProcessingValue(UUID.randomUUID().toString(), CODE_MAX_ATTENDANCE_LAST_CODE, "0") }

            val opMax = Optional.ofNullable(tmpMax).orElseGet { ProcessingValue(UUID.randomUUID().toString(), CODE_MAX_CHECK_TIME, "1970-01-01 00:00:00") }
            var maxCheckTimeStr = opMax.value
            var lastCode = opLastCode.value

            var maxCheckTime = DateTimeUtil.parse(maxCheckTimeStr, "yyyy-MM-dd HH:mm:ss")


            val job = launch(CommonPool) {
                client.getConnection { connRes ->
                    if (connRes.succeeded()) {
                        val conn = connRes.result()
                        LOGGER.debug("got connection:$conn")

                        conn.queryStream("select hck.UCheckCode,hui.UserCode,hui.ChineseNAME ,hck.CHECKTIME from dbo.HR_CHECKINOUT hck join HR_UserInfo hui on hck.USERID=hui.Id where hck.CHECKTIME>='$maxCheckTimeStr' and hck.UCheckCode<>$lastCode ORDER BY hck.CHECKTIME", { res ->
                            LOGGER.debug("got query result.")
                            val attendanceRecordList = mutableListOf<AttendanceRecordEntry>()
                            LOGGER.debug("query succeed?${res.succeeded()}")
                            if (res.succeeded()) {
                                val rowStream = res.result()
                                LOGGER.debug("rowstream:$rowStream")

                                rowStream.resultSetClosedHandler { rowStream.moreResults() }.handler { row1 ->
                                    LOGGER.debug("row:$row1")
                                    val checkCode = row1.getLong(0)
                                    val userId = row1.getString(1)
                                    val tmpCheckTime = row1.getInstant(3)

                                    val checkTime = Optional.ofNullable(tmpCheckTime).orElseGet { Instant.now() }

                                    val ctDate = Date(checkTime.toEpochMilli())


                                    if (maxCheckTime.before(ctDate)) {
                                        maxCheckTime = ctDate
                                        lastCode = "$checkCode"
                                        maxCheckTimeStr = DateTimeUtil.formatDate(ctDate, "yyyy-MM-dd HH:mm:ss")
                                    }


                                    val ar = AttendanceRecordEntry()
                                    ar.checkDate = DateTimeUtil.formatDate(ctDate, "yyyy-MM-dd")
                                    ar.checkTime = ctDate
                                    ar.userId = userId

                                    attendanceRecordList.add(ar)
                                    LOGGER.debug("attendance record:$ar")
                                }.endHandler {
                                    attendanceRecordEntryRepository.save(attendanceRecordList)

                                    opMax.value = maxCheckTimeStr
                                    processingValueRepository.save(opMax)

                                    opLastCode.value = lastCode
                                    processingValueRepository.save(opLastCode)
                                    LOGGER.debug("import attendance record DONE.")
                                    client.close()
                                }.exceptionHandler { LOGGER.warn("Failed to query,msg: ${it.message}") }
                            } else {
                                LOGGER.warn("Failed.cause: ${res.cause().message}")

                            }
                        })
                    } else {
                        LOGGER.warn("can not get connection")
                    }
                }
            }
            job.join()
        }

    }
}