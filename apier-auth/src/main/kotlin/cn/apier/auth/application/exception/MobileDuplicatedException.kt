package cn.apier.auth.application.exception

import cn.apier.common.exception.BaseException

class MobileDuplicatedException : BaseException {
    constructor() : super(ErrorDefinitions.CODE_MOBILE_DUPLICATED, ErrorDefinitions.MSG_MOBILE_DUPLICATED)
}