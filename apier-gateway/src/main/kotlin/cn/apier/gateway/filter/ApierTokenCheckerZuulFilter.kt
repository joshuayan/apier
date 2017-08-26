package cn.apier.gateway.filter

import cn.apier.common.api.Result
import cn.apier.gateway.application.exception.ErrorDefinitions
import cn.apier.gateway.common.AuthTool
import cn.apier.gateway.common.TokenHolder
import cn.apier.gateway.config.FilterConfiguration
import com.alibaba.fastjson.JSON
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.exception.ZuulException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class ApierTokenCheckerZuulFilter : ZuulFilter() {

    @Autowired
    private lateinit var filterConfig: FilterConfiguration

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(ApierTokenCheckerZuulFilter::class.java)
    }

    override fun run(): Any {

        val requestContext = RequestContext.getCurrentContext()

        val request = requestContext.request
        val response = requestContext.response
        if (!checkIfValidRequest(request)) {

            requestContext.setSendZuulResponse(false)
            LOGGER.debug("redirect to needToken")
//            response?.sendRedirect("/auth/error/needToken")

            val jsonObj = JSON.toJSON(Result.FAIL(ErrorDefinitions.CODE_AUTH_NEED_TOKEN, ErrorDefinitions.MSG_AUTH_NEED_TOKEN))
                    .toString()
            LOGGER.debug("json result[$jsonObj]")
            response.setHeader("Content-type", "application/json;charset=UTF-8");
//            response?.characterEncoding = "UTF-8";
            response.writer.write(jsonObj.toString())
            response.flushBuffer()
            throw ZuulException(ErrorDefinitions.MSG_AUTH_NEED_TOKEN, 401, ErrorDefinitions.MSG_AUTH_NEED_TOKEN)

        }


        return Any()
    }

    override fun shouldFilter(): Boolean {

        val excludeUrls = this.filterConfig.tokenChecker
        val url = RequestContext.getCurrentContext().request.requestURL.toString()
        return !excludeUrls.any { it.startsWith(url) }
    }

    override fun filterType(): String = "pre"

    override fun filterOrder(): Int = -2
    private fun checkIfValidRequest(request: HttpServletRequest?): Boolean {

        val token = request?.getParameter("token")
        val valid = !token.isNullOrBlank()

        if (valid) TokenHolder.setCurrentToken(token!!)

        return valid
    }
}


@Component
class ApierSignInCheckerZuulFilter : ZuulFilter() {

    @Autowired
    private lateinit var filterConfig: FilterConfiguration

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(ApierSignInCheckerZuulFilter::class.java)
    }

    override fun run(): Any {


        LOGGER.debug("start sign in check.")
        val signed: Boolean = AuthTool.checkIfSigned()

        LOGGER.debug("signed:$signed")

        if (!signed) {
            RequestContext.getCurrentContext().setSendZuulResponse(false)


//            response?.sendRedirect("/auth/error/needSignIn")
            val jsonObj = JSON.toJSON(Result.FAIL(ErrorDefinitions.CODE_AUTH_NEED_SIGNIN, ErrorDefinitions.MSG_AUTH_NEED_SIGNIN))
                    .toString()

            LOGGER.debug("json result[$jsonObj]")
//            response?.outputStream?.write(jsonObj.toByteArray(charset = Charset.forName("utf-8")))
            RequestContext.getCurrentContext().response.setHeader("Content-type", "application/json;charset=UTF-8");
//            response?.characterEncoding = "UTF-8";
            RequestContext.getCurrentContext().response.writer.write(jsonObj.toString())
            RequestContext.getCurrentContext().response.flushBuffer()
            throw ZuulException(ErrorDefinitions.MSG_AUTH_NEED_SIGNIN, 401, ErrorDefinitions.MSG_AUTH_NEED_SIGNIN)
        }

        return Any()
    }

    override fun shouldFilter(): Boolean {
        val excludeUrls = this.filterConfig.signInChecker
        val url = RequestContext.getCurrentContext().request.requestURL.toString()
        return !excludeUrls.any { it.startsWith(url) }
    }

    override fun filterType(): String {
        return "pre"
    }

    override fun filterOrder(): Int {
        return -1
    }

}