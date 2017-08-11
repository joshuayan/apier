package cn.apier.auth.query.repository

import cn.apier.auth.query.entry.ClientApplicationEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientApplicationEntryRepository : JpaRepository<ClientApplicationEntry, String> {
    fun findByAppKey(appKey: String): ClientApplicationEntry?
}