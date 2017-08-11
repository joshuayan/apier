package cn.apier.auth.api

import cn.apier.auth.application.service.ClientApplicationService
import cn.apier.common.api.Result
import cn.apier.common.extension.parameterRequired
import cn.apier.common.util.ExecuteTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/client")
class ClientApplicationApi {

    @Autowired
    private lateinit var clientApplicationService: ClientApplicationService

    @PostMapping("/new")
    fun addNewClientApplication(name: String): Result<Any> = ExecuteTool.executeWithTry {
        parameterRequired(name, "name")
        this.clientApplicationService.addNewClient(name)
    }
}