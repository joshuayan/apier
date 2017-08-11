package cn.apier.auth.application.command

import java.util.*

data class CreateClientApplicationCommand(val enterpriseId: String, val uid: String, val name: String, val appKey: String, val appSecret: String
                                   , val deleted: Boolean, val createdAt: Date)