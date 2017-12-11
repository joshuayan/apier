package cn.apier.task.application.service

import cn.apier.common.extension.invalidOperation
import cn.apier.common.extension.nullOrThen
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.UUIDUtil
import cn.apier.gateway.context.ApiGatewayContext
import cn.apier.task.application.command.CreateTaskCommand
import cn.apier.task.application.command.FinishTaskCommand
import cn.apier.task.application.command.ReopenTaskCommand
import cn.apier.task.application.command.UpdateTaskCommand
import cn.apier.task.query.dao.TaskEntryRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.config.Configuration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
open class TaskService {

    private val LOGGER = LoggerFactory.getLogger(TaskService::class.java)
    @Autowired
    private lateinit var taskEntryRepository: TaskEntryRepository

    @Autowired
    private lateinit var commandGateway: CommandGateway

    @Autowired
    private lateinit var configuration: Configuration


    fun newTask(content: String, deadLine: Date?) {

        LOGGER.debug("task content:$content")
        parameterRequired(content, "content")
        val userId = ApiGatewayContext.currentContext.currentUser()
        this.commandGateway.sendAndWait<Unit>(CreateTaskCommand(UUIDUtil.commonUUID(), userId!!, content, deadLine))
    }

    fun updateTask(uid: String, content: String, deadLine: Date?) {
        parameterRequired(uid, "uid")
        parameterRequired(content, "content")
        this.taskEntryRepository.findOne(uid).nullOrThen({ invalidOperation() }, { this.commandGateway.sendAndWait(UpdateTaskCommand(uid, content, deadLine)) })
    }

    fun finishTask(uid: String) {
        parameterRequired(uid, "uid")
        this.taskEntryRepository.findOne(uid).nullOrThen({ invalidOperation() }, { this.commandGateway.sendAndWait(FinishTaskCommand(uid)) })
    }

    fun reopenTask(uid: String) {
        parameterRequired(uid, "uid")
        this.taskEntryRepository.findOne(uid).nullOrThen({ invalidOperation() }, { this.commandGateway.sendAndWait(ReopenTaskCommand(uid)) })
    }

}
