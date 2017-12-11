package cn.apier.task

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
open class TaskApplication


fun main(args: Array<String>) {
    SpringApplication.run(TaskApplication::class.java, *args)
}



//
//fun main(args: Array<String>) {
//    embeddedServer(Netty, 8080) {
//        routing {
//            get("/") {
//                call.respondText("Hello, world!", ContentType.Text.Html)
//            }
//        }
//    }.start(wait = true)
//}