package cn.apier.task.api

import cn.apier.common.api.Result
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.ExecuteTool
import cn.apier.task.application.service.TaskService
import cn.apier.task.query.dao.TaskEntryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/task")
open class TaskController {
    @Autowired
    private lateinit var taskService: TaskService

    @Autowired
    private lateinit var taskEntryRepository: TaskEntryRepository

    @PostMapping("/new")
    fun newTask(content: String, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") deadLine: Date?): Result<Any> =
            ExecuteTool.executeWithTry {
                this.taskService
                        .newTask(content, deadLine)
            }


    @PostMapping("/update")
    fun updateTask(uid: String, content: String, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") deadLine: Date?): Result<Any> = ExecuteTool.executeWithTry {
        this.taskService.updateTask(uid, content, deadLine)
    }

    @PostMapping("/finish")
    fun finishTask(uid: String): Result<Any> = ExecuteTool.executeWithTry {
        parameterRequired(uid, "uid")
        this.taskService.finishTask(uid)
    }

    @PostMapping("/reopen")
    fun reopenTask(uid: String): Result<Any> = ExecuteTool.executeWithTry {
        parameterRequired(uid, "uid")
        this.taskService.reopenTask(uid)
    }


    @GetMapping("/test")
    fun test():Mono<String> =  Mono.just("test for web flux")


    @GetMapping("/list")
    fun list(finished: Boolean): Result<Any> = ExecuteTool.executeQueryWithTry { this.taskEntryRepository.findByFinishedOrderByCreatedAt(finished) }
}