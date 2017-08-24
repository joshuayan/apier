package cn.apier.auth.interceptor

import cn.apier.auth.application.exception.ErrorDefinitions
import cn.apier.auth.common.AuthTool
import cn.apier.auth.common.TokenHolder
import cn.apier.common.api.Result
import cn.apier.common.extension.isNull
import com.alibaba.fastjson.JSON
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.nio.charset.Charset
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SignedInterceptor : HandlerInterceptorAdapter() {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(SignedInterceptor::class.java)
    }

    override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {

        LOGGER.debug("start sign in check.")
        val signed: Boolean = AuthTool.checkIfSigned()

        LOGGER.debug("signed:$signed")

        if (!signed) {

//            response?.sendRedirect("/auth/error/needSignIn")
            val jsonObj=JSON.toJSON(Result.FAIL(ErrorDefinitions.CODE_AUTH_NEED_SIGNIN,ErrorDefinitions.MSG_AUTH_NEED_SIGNIN))
                    .toString()

            LOGGER.debug("json result[$jsonObj]")
//            response?.outputStream?.write(jsonObj.toByteArray(charset = Charset.forName("utf-8")))
            response?.setHeader("Content-type", "application/json;charset=UTF-8");
//            response?.characterEncoding = "UTF-8";
            response?.writer?.write(jsonObj.toString())
            response?.flushBuffer()
        }
        return signed
    }


}


class TokenCheckInterceptor : HandlerInterceptorAdapter() {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(TokenCheckInterceptor::class.java)
    }

    override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
        var needToken = false

        LOGGER.debug("start token check.")
        if (!checkIfValidRequest(request)) {
            needToken = true
            LOGGER.debug("redirect to needToken")
//            response?.sendRedirect("/auth/error/needToken")

            val jsonObj=JSON.toJSON(Result.FAIL(ErrorDefinitions.CODE_AUTH_NEED_TOKEN,ErrorDefinitions.MSG_AUTH_NEED_TOKEN))
                    .toString()
            SignedInterceptor.LOGGER.debug("json result[$jsonObj]")
            response?.setHeader("Content-type", "application/json;charset=UTF-8");
//            response?.characterEncoding = "UTF-8";
            response?.writer?.write(jsonObj.toString())
            response?.flushBuffer()
        }

        return !needToken
    }

    private fun checkIfValidRequest(request: HttpServletRequest?): Boolean {

        val token = request?.getParameter("token")
        val valid = !token.isNullOrBlank()

        if (valid) TokenHolder.setCurrentToken(token!!)

        return valid
    }
}