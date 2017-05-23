package cn.apier.common.exception;

/**
 * Created by yanjunhua on 16/5/30.
 */
final public class CommonException extends BaseException
{
    public CommonException(String code)
    {
        super(code);
    }

    public CommonException(String code, String message)
    {
        super(code, message);
    }

    public static CommonException parameterError()
    {
        return new CommonException(CommonErrorCodes.COMMON_PARAMETER_ERROR, CommonErrorCodes.COMMON_MESSAGE_PARAMETER_ERROR);
    }

    public static CommonException invalidOperation()
    {
        return new CommonException(CommonErrorCodes.COMMON_OPERATION_INVALID, CommonErrorCodes.COMMON_MESSAGE_OPERATION_INVALID);
    }

    public static CommonException unknownException()
    {
        return new CommonException(CommonErrorCodes.COMMON_UNKNOWN_EXCEPTION, CommonErrorCodes.COMMON_MESSAGE_UNKNOWN_EXCEPTION);
    }

    public static CommonException nameDuplicatedException(String name)
    {
        return new CommonException(CommonErrorCodes.COMMON_NAME_DUPLICATED, String.format(CommonErrorCodes.COMMON_MESSAGE_TEMPLATE_NAME_DUPLICATED, name));

    }

    public static CommonException codeDuplicatedException(String code)
    {
        return new CommonException(CommonErrorCodes.COMMON_CODE_DUPLICATED, String.format(CommonErrorCodes.COMMON_MESSAGE_TEMPLATE_CODE_DUPLICATED, code));

    }


    public static CommonException NPE()
    {
        return new CommonException(CommonErrorCodes.COMMON_NPE, CommonErrorCodes.COMMON_MESSAGE_NPE);

    }
}
