package cn.apier.common.exception;

/**
 * Created by yanjunhua on 16/5/30.
 */
public class BaseException extends RuntimeException
{
    private String code;
    protected Object data;

    public BaseException()
    {
    }

    public BaseException(String code, String message)
    {
        this(code, message, null);
    }

    public BaseException(String code, String message, Object data)
    {
        super(message);
        this.code = code;
        this.data = data;
    }

    public Object getData()
    {
        return data;
    }

    public BaseException(String code)
    {
        this(code, code);
    }

    public String getCode()
    {
        return code;
    }
}
