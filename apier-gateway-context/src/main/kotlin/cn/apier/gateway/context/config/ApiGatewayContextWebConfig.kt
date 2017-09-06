package cn.apier.gateway.context.config

import cn.apier.gateway.context.ApiGatewayContextInterceptor
//import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

class ApiGatewayContextWebConfig : WebMvcConfigurerAdapter() {
    override fun addInterceptors(registry: InterceptorRegistry?) {
        registry?.addWebRequestInterceptor(ApiGatewayContextInterceptor())
    }
}