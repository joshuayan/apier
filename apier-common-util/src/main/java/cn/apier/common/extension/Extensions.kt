package cn.apier.common.extension

import cn.apier.common.exception.CommonException
import java.util.*

/**
 * Created by yanjunhua on 2017/6/22.
 */


fun <T> T.nullOrThen(nullProcessor: () -> Unit, then: (T) -> Unit): T {
    when (this) {
        null -> nullProcessor()
        else -> then(this)
    }
    return this
}


fun <T> T.isNull(): Boolean = Objects.isNull(this)
fun <T> T.nonNull(): Boolean = Objects.nonNull(this)


fun invalidOperation(): Unit {
    throw CommonException.invalidOperation()
}


fun <T> T.invalidOperationIfNull(): T {
    if (Objects.isNull(this)) throw CommonException.invalidOperation()
    return this
}

fun parameterRequired(value: Any?, name: String) {
    when (value) {
        is String -> if (value.isNullOrBlank()) {
            throw  CommonException.parameterRequired(name)
        }
        else -> if (Objects.isNull(value)) throw  CommonException.parameterRequired(name)
    }
}
