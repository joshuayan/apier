package cn.apier.auth.application.service

import cn.apier.auth.application.command.CreateUserCommand
import cn.apier.auth.application.exception.MobileDuplicatedException
import cn.apier.auth.query.repository.UserEntryRepository
import cn.apier.auth.query.service.UserEntryQueryService
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.ExecuteTool
import cn.apier.common.util.UUIDUtil
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class UserService {
    @Autowired
    private lateinit var commandGateway: CommandGateway

    @Autowired
    private lateinit var userEntryQueryService: UserEntryQueryService

    @Autowired
    private lateinit var userEntryRepository: UserEntryRepository


    fun register(mobile: String, password: String) {
        parameterRequired(mobile, "mobile")
        parameterRequired(password, "password")
        ExecuteTool
                .conditionalException({ this.userEntryQueryService.checkIfDuplicated(mobile) }, { MobileDuplicatedException() })
        this.commandGateway.sendAndWait<Unit>(CreateUserCommand(UUIDUtil.commonUUID(), mobile, password, DateTimeUtil.now()))
    }


    fun disableUser(uid: String) {
        parameterRequired(uid, "uid")
        this.userEntryRepository.exists(uid)
    }

}