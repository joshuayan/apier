package cn.apier.task.domain.event

import java.util.*

data class TaskCreatedEvent(val uid: String,val userId:String, val content: String, val finished: Boolean, val deadLine: Date?, val createdAt: Date)


data class TaskUpdatedEvent(val uid: String, val content: String, val deadLine: Date?)

data class TaskFinishedEvent(val uid: String)

data class TaskReopenedEvent(val uid: String)