package cn.apier.auth.interceptor

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

class AuthWebConfiguration : WebMvcConfigurerAdapter() {
    override fun addInterceptors(registry: InterceptorRegistry?) {
        registry?.addInterceptor(TokenCheckInterceptor())?.excludePathPatterns("/auth/token")?.excludePathPatterns("/auth/error/*")
        registry?.addInterceptor(SignedInterceptor())?.excludePathPatterns("/auth/signIn")?.excludePathPatterns("/auth/error/*")
                ?.excludePathPatterns("/auth/token")?.excludePathPatterns("/user/register")
    }
}