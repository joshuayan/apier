package cn.apier.task.domain.model

import cn.apier.common.domain.model.BaseModel
import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.ExecuteTool
import cn.apier.task.domain.event.TaskCreatedEvent
import cn.apier.task.domain.event.TaskFinishedEvent
import cn.apier.task.domain.event.TaskReopenedEvent
import cn.apier.task.domain.event.TaskUpdatedEvent
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class Task : BaseModel {

    private var content: String = ""
    private var deadLine: Date? = null
    private var finished: Boolean = false

    private constructor()

    constructor(uid: String, content: String, deadLine: Date?) {
        AggregateLifecycle.apply(TaskCreatedEvent(uid, content, false, deadLine, DateTimeUtil.now()))
    }


    fun update(content: String, deadLine: Date?) {
        AggregateLifecycle.apply(TaskUpdatedEvent(uid, content, deadLine))
    }


    fun finish() {
        ExecuteTool.invalidOperationIf { this.finished }
        AggregateLifecycle.apply(TaskFinishedEvent(uid))
    }

    fun reopen() {
        ExecuteTool.invalidOperationIf { !this.finished }
        AggregateLifecycle.apply(TaskReopenedEvent(uid))
    }

    @EventSourcingHandler
    private fun onFinished(taskFinishedEvent: TaskFinishedEvent) {
        this.finished = true
    }

    @EventSourcingHandler
    private fun onReopened(taskReOpenedEvent: TaskReopenedEvent) {
        this.finished = false
    }


    @EventSourcingHandler
    private fun onUpdated(taskUpdatedEvent: TaskUpdatedEvent) {
        this.content = taskUpdatedEvent.content
        this.deadLine = taskUpdatedEvent.deadLine
    }

    @EventSourcingHandler
    private fun onCreated(taskCreatedEvent: TaskCreatedEvent) {
        this.uid = taskCreatedEvent.uid
        this.content = taskCreatedEvent.content
        this.deadLine = taskCreatedEvent.deadLine
        this.createdAt = taskCreatedEvent.createdAt
        this.finished = false
    }

}