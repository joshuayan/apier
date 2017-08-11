package cn.apier.auth.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "apier.auth")
class AuthConfig {
    var tokenExpiredTimeInMs: Long = 3600000
    var loginExpiredTimeInMs: Long = 3600000

}