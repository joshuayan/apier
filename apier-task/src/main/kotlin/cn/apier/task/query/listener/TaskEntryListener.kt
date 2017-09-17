package cn.apier.task.query.listener

import cn.apier.task.domain.event.TaskCreatedEvent
import cn.apier.task.domain.event.TaskFinishedEvent
import cn.apier.task.domain.event.TaskReopenedEvent
import cn.apier.task.domain.event.TaskUpdatedEvent
import cn.apier.task.query.dao.TaskEntryRepository
import cn.apier.task.query.entry.TaskEntry
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TaskEntryListener {

    @Autowired
    private lateinit var taskEntryRepository: TaskEntryRepository


    @EventHandler
    fun onCreation(taskCreatedEvent: TaskCreatedEvent) {
        TaskEntry(taskCreatedEvent.uid, taskCreatedEvent.userId, taskCreatedEvent.content, taskCreatedEvent.finished, taskCreatedEvent.deadLine, taskCreatedEvent.createdAt)
                .also { this.taskEntryRepository.save(it) }
    }

    @EventHandler
    fun onUpdated(taskUpdatedEvent: TaskUpdatedEvent) {
        this.taskEntryRepository.findOne(taskUpdatedEvent.uid).also { it.content = taskUpdatedEvent.content;it.deadLine = taskUpdatedEvent.deadLine; }
                .also { this.taskEntryRepository.save(it) }
    }

    @EventHandler
    fun onFinished(taskFinishedEvent: TaskFinishedEvent) {
        this.taskEntryRepository.findOne(taskFinishedEvent.uid).also { it.finished = true }.also { this.taskEntryRepository.save(it) }
    }

    @EventHandler
    fun onReopened(taskReopenedEvent: TaskReopenedEvent) {
        this.taskEntryRepository.findOne(taskReopenedEvent.uid).also { it.finished = false }.also { this.taskEntryRepository.save(it) }
    }
}