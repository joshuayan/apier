package cn.apier.attendance.query

import org.axonframework.queryhandling.QueryBus
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class AttendanceRecordEntryService {
    lateinit var queryBus: QueryGateway
}