package cn.apier.gateway.context

import org.springframework.ui.ModelMap
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor
import java.lang.Exception
import java.util.*

class ApiGatewayContextInterceptor : WebRequestInterceptor {
    override fun preHandle(request: WebRequest?) {

        val payloads = request?.getHeader(ContextConstant.KEY_HEADER_PAYLOAD)
        Optional.ofNullable(payloads).ifPresent {
            it.split("&").forEach { item ->
                run {
                    val payload = item.split("=")
                    ApiGatewayContext.currentContext.addPayload(payload[0], payload[1])
                }
            }
        }
    }

    override fun postHandle(p0: WebRequest?, p1: ModelMap?) {
    }

    override fun afterCompletion(p0: WebRequest?, p1: Exception?) {
    }
}