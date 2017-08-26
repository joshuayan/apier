package cn.apier.gateway.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "gateway.filter.url.exclude")
class FilterConfiguration {

    lateinit var tokenChecker: List<String>

    lateinit var signInChecker: List<String>

}