package cn.apier.auth.application.command

data class CreateAuthTokenCommand(val enterpriseId: String, val appKey: String)