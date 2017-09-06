package cn.apier.gateway.context

class ApiGatewayContext {
    private val payloads: MutableMap<String, String> = mutableMapOf()


    private constructor()

    companion object {
        private val contexts: ThreadLocal<ApiGatewayContext> = ThreadLocal.withInitial {
            ApiGatewayContext()
        }
        val currentContext: ApiGatewayContext = contexts.get()
    }


    fun getPayload(key: String): String? = this.payloads[key]

    fun addPayload(key: String, value: String) {
        this.payloads.put(key, value)
        when (key) {
            ContextConstant.KEY_HEADER_PAYLOAD_TOKEN -> this.setCurrentToken(value)
            ContextConstant.KEY_HEADER_PAYLOAD_USER -> this.setCurrentUser(value)
        }


    }


    fun currentToken(): String? = this.payloads[ContextConstant.KEY_CURRENT_TOKEN]

    fun currentUser(): String? = this.payloads[ContextConstant.KEY_CURRENT_USER]

    fun setCurrentToken(token: String) = this.payloads.put(ContextConstant.KEY_CURRENT_TOKEN, token)

    fun setCurrentUser(user: String) = this.payloads.put(ContextConstant.KEY_CURRENT_USER, user)

}