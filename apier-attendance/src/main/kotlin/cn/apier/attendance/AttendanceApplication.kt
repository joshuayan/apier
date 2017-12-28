package cn.apier.attendance

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
@MapperScan("cn.apier.attendance.query.dao")
class AttendanceApplication

fun main(args: Array<String>) {
    SpringApplication.run(AttendanceApplication::class.java, *args)
}
