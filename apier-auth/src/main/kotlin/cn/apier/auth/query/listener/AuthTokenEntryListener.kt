package cn.apier.auth.query.listener

import cn.apier.auth.common.AuthConfig
import cn.apier.auth.domain.event.AuthTokenCreatedEvent
import cn.apier.auth.query.entry.AuthTokenEntry
import cn.apier.auth.query.repository.AuthTokenEntryRepository
import cn.apier.common.cache.MemoryCache
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class AuthTokenEntryListener {
    //    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, AuthTokenEntry>
    @Autowired
    private lateinit var authConfig: AuthConfig

    @Autowired
    private lateinit var authTokenEntryRepository: AuthTokenEntryRepository

    @EventHandler
    fun onCreated(authTokenCreatedEvent: AuthTokenCreatedEvent) {
        AuthTokenEntry(authTokenCreatedEvent.enterpriseId,authTokenCreatedEvent.uid, authTokenCreatedEvent.code, authTokenCreatedEvent.appKey, authTokenCreatedEvent.expiredAt
                , authTokenCreatedEvent.createdAt, authTokenCreatedEvent.deleted).also { this.authTokenEntryRepository.save(it) }
                .also { cacheTokenInMemory(it) }
    }

    private fun cacheTokenInRedis(authTokenEntry: AuthTokenEntry) {
        redisTemplate.opsForValue().set(authTokenEntry.code, authTokenEntry, authConfig.tokenExpiredTimeInMs, TimeUnit.MILLISECONDS)
    }

    private fun cacheTokenInMemory(authTokenEntry: AuthTokenEntry) {
        MemoryCache.default.cache(authTokenEntry.code, authTokenEntry, expiredInMs = authConfig.tokenExpiredTimeInMs)

    }
}