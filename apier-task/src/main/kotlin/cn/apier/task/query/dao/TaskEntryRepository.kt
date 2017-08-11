package cn.apier.task.query.dao

import cn.apier.task.query.entry.TaskEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskEntryRepository : JpaRepository<TaskEntry, String>