package cn.apier.task.query.entry

import cn.apier.common.util.DateTimeUtil
import org.codehaus.jackson.annotate.JsonProperty
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class TaskEntry(@Id var uid: String = "", var userId: String = "", var content: String = "", var finished: Boolean = false, var deadLine: Date? = null, var createdAt: Date = DateTimeUtil.now())