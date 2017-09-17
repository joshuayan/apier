package cn.apier.task.application.command

import java.util.*

data class CreateTaskCommand(val uid: String,val userId:String, val content: String, val deadLine: Date?)

data class UpdateTaskCommand(val uid: String, val content: String, val deadLine: Date?)

data class FinishTaskCommand(val uid: String)

data class ReopenTaskCommand(val uid: String)