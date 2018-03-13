package cn.apier.gateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties


@SpringBootApplication
@EnableConfigurationProperties
class ApierGatewayApplication2

fun main(args: Array<String>) {
    SpringApplication.run(ApierGatewayApplication2::class.java, *args)
}