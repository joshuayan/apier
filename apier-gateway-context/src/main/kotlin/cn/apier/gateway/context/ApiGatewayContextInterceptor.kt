package cn.apier.gateway.context

import org.slf4j.LoggerFactory
import org.springframework.ui.ModelMap
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor
import java.lang.Exception
import java.util.*

class ApiGatewayContextInterceptor : WebRequestInterceptor {

    private val LOGGER=LoggerFactory.getLogger(ApiGatewayContextInterceptor::class.java)
    override fun preHandle(request: WebRequest?) {

        val payloads = request?.getHeader(ContextConstant.KEY_HEADER_PAYLOAD)

        payloads?.let {
            it.split("&").forEach { item ->
                val payload = item.split("=")
                LOGGER.debug("payloads:${payload.joinToString("=")}")
                ApiGatewayContext.currentContext.addPayload(payload[0], payload[1])
            }
        }
//        Optional.ofNullable(payloads).ifPresent {
//            it.split("&").forEach { item ->
//                run {
//                    val payload = item.split("=")
//                    ApiGatewayContext.currentContext.addPayload(payload[0], payload[1])
//                }
//            }
//        }
    }

    override fun postHandle(p0: WebRequest?, p1: ModelMap?) {
    }

    override fun afterCompletion(p0: WebRequest?, p1: Exception?) {
    }
}