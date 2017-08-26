package cn.apier.gateway.application.exception

import cn.apier.common.exception.BaseException


class BadSignatureException : BaseException(ErrorDefinitions.CODE_AUTH_BAD_SIGNATURE, ErrorDefinitions.MSG_AUTH_BAD_SIGNATURE)