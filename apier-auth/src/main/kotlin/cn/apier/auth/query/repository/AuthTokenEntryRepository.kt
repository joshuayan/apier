package cn.apier.auth.query.repository

import cn.apier.auth.query.entry.AuthTokenEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthTokenEntryRepository : JpaRepository<AuthTokenEntry, String> {
    fun findByAppKeyAndExpiredAtGreaterThanAndDeletedFalse(appKey: String, expiredAt: Date): AuthTokenEntry?
}