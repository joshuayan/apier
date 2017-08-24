package cn.apier.auth.application.command

import java.util.*

data class CreateUserCommand(val uid: String, val mobile: String, val password: String, val createdAt: Date)

data class UpdatePasswordCommand(val uid: String, val password: String)

data class EnableUser(val uid: String)

data class DisableUser(val uid: String)

data class SignInCommand(val mobile: String,val password: String)