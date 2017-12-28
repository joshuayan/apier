package cn.apier.common.extension

import cn.apier.common.api.Result
import cn.apier.common.exception.BaseException
import cn.apier.common.util.ExecuteTool
import java.util.*


class Validator {

    val config: ValidationConfig = ValidationConfig()


    fun validate() {
        config.rules.forEach {
            ExecuteTool.conditionalException({ it.conditionSupplier() }, { it.exceptionSupplier() })
        }
    }

}

class ValidationConfig {

    val rules: MutableList<ValidationRule> = ArrayList()
    fun rule(block: ValidationRule.() -> Unit) {
        val validationRule = ValidationRule()
        validationRule.block()
        this.rules.add(validationRule)
    }
}


class ValidationRule {

    var conditionSupplier: () -> Boolean = { true }
        private set

    var exceptionSupplier: () -> BaseException = { BaseException("Unknown", " Need to set validation exception") }
        private set

    fun ifTrue(block: () -> Boolean) {
        this.conditionSupplier = block
    }

    fun ifFalse(block: () -> Boolean) {
        ifTrue { !block() }
    }

    fun ifNull(block: () -> Any?) {
        this.conditionSupplier = { Objects.isNull(block()) }
    }

    fun ifNonNull(block: () -> Any?) {
        this.conditionSupplier = { Objects.nonNull(block()) }
    }

    fun exception(block: () -> BaseException) {
        this.exceptionSupplier = block
    }
}


fun validationRules(block: ValidationConfig.() -> Unit): Validator {
    val validator = Validator()
    validator.config.block()
    return validator
}


fun main(args: Array<String>) {
    validationRules {
        rule {
            ifTrue { false }
            exception { BaseException("code", "test") }
        }
        rule {
            ifNull { null }
            exception { BaseException("code1", "NPE") }
        }
    }.validate()
}


fun <T> query(block: () -> T?): Result<T> = ExecuteTool.executeQueryWithTry { block() }
fun <T : List<Any>> queryWithPage(block: () -> T?): Result<T> = ExecuteTool.executeQueryWithTry { ExecuteTool.queryPageData { block(); } }


fun execute(block: () -> Unit): Result<Any> = ExecuteTool.executeWithTry { block() }