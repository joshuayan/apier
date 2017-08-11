package cn.apier.auth.query.repository

import cn.apier.auth.query.entry.UserEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserEntryRepository : JpaRepository<UserEntry, String> {
    fun findByMobile(mobile: String): UserEntry
}