package cn.apier.gateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableZuulProxy
@EnableConfigurationProperties
@EnableFeignClients

class ApierGatewayApplication


fun main(args: Array<String>) {
    SpringApplication.run(ApierGatewayApplication::class.java, *args)
}