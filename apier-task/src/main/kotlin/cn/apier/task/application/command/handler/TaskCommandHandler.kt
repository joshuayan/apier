package cn.apier.task.application.command.handler

import cn.apier.task.application.command.CreateTaskCommand
import cn.apier.task.application.command.FinishTaskCommand
import cn.apier.task.application.command.ReopenTaskCommand
import cn.apier.task.application.command.UpdateTaskCommand
import cn.apier.task.domain.model.Task
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.config.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
open class TaskCommandHandler {

    @Autowired
    private lateinit var configuration: Configuration

    @CommandHandler
    fun processCreation(createTaskCommand: CreateTaskCommand) {
        this.configuration.repository(Task::class.java).newInstance { Task(createTaskCommand.uid, createTaskCommand.content, createTaskCommand.deadLine) }
    }

    @CommandHandler
    fun processUpdate(updateTaskCommand: UpdateTaskCommand) {
        this.configuration.repository(Task::class.java).load(updateTaskCommand.uid).execute { it.update(updateTaskCommand.content, updateTaskCommand.deadLine) }
    }

    @CommandHandler
    fun processFinish(finishTaskCommand: FinishTaskCommand) {
        this.configuration.repository(Task::class.java).load(finishTaskCommand.uid).execute { it.finish() }
    }

    @CommandHandler
    fun processReopen(reopenTaskCommand: ReopenTaskCommand) {
        this.configuration.repository(Task::class.java).load(reopenTaskCommand.uid).execute { it.reopen() }
    }

}