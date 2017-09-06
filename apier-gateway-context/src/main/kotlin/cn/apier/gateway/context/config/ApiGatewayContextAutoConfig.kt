package cn.apier.gateway.context.config

import cn.apier.gateway.context.ApiGatewayContextInterceptor
import org.springframework.context.annotation.Bean


class ApiGatewayContextAutoConfig {
//    @Bean
//    fun contextInterceptor() = ApiGatewayContextInterceptor()

    @Bean
    fun webConfig()=ApiGatewayContextWebConfig()
}