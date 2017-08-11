package cn.apier.auth.query.service

import cn.apier.auth.query.entry.AuthTokenEntry
import cn.apier.auth.query.repository.AuthTokenEntryRepository
import cn.apier.common.util.DateTimeUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthTokenEntryQueryService {

    @Autowired
    private lateinit var authTokenEntryRepository: AuthTokenEntryRepository

    fun queryValidTokenByAppKey(appKey: String): AuthTokenEntry? = this.authTokenEntryRepository.findByAppKeyAndExpiredAtGreaterThanAndDeletedFalse(appKey, DateTimeUtil.now())

    fun queryToken(appKey: String, timestampInMs: String, signature: String) {

    }

}