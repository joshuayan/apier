package cn.apier.common

import io.vertx.core.Vertx

object VertxContext {
    private val vertxInstance: Vertx = Vertx.vertx()
    fun vertx() = vertxInstance
}