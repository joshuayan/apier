package cn.apier.auth.query.service

import cn.apier.auth.query.repository.UserEntryRepository
import cn.apier.common.extension.nonNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserEntryQueryService {

    @Autowired
    private lateinit var userEntryRepository: UserEntryRepository

    fun checkIfDuplicated(mobile: String): Boolean = this.userEntryRepository.findByMobile(mobile).nonNull()
}