package cn.apier.auth.application.service

import cn.apier.auth.application.command.CreateClientApplicationCommand
import cn.apier.auth.common.ConstantObject
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.UUIDUtil
import cn.apier.common.util.Utils
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClientApplicationService {

    @Autowired
    private lateinit var commandGateway: CommandGateway

    fun addNewClient(name: String) {
        parameterRequired(name, "name")

        val enterpriseId: String = "78"
        val appKey: String = buildAppKey(name, enterpriseId)
        val appSecret: String = buildAppSecret(appKey)
        this.commandGateway.sendAndWait<Unit>(CreateClientApplicationCommand(enterpriseId, UUIDUtil.commonUUID(), name, appKey, appSecret, false, DateTimeUtil.now()))
    }

    private fun buildAppKey(name: String, enterpriseId: String) = Utils.md5(name + System.currentTimeMillis() + enterpriseId)

    private fun buildAppSecret(appKey: String) = Utils.md5(ConstantObject.SALT_TOKEN + appKey + ConstantObject.SALT_TOKEN)

}