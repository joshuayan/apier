package cn.apier.gateway.context.config

import org.springframework.context.annotation.Bean


class ApiGatewayContextAutoConfig {
//    @Bean
//    fun contextInterceptor() = ApiGatewayContextInterceptor()

    @Bean
    fun webConfig()=ApiGatewayContextWebConfig()
}