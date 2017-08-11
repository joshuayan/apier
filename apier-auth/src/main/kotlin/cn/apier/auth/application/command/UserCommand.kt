package cn.apier.auth.application.command

import java.util.*

data class CreateUserCommand(val uid: String, val mobile: String, val password: String,val createdAt: Date)

data class UpdatePasswordCommand(val uid: String, val password: String)

