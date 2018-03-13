package cn.apier.permission.domain.model

import cn.apier.common.domain.model.EnterpriseEnabledBaseModel

class EnterpriseUser : EnterpriseEnabledBaseModel() {
    private var name: String = ""
    private var password: String = ""
}