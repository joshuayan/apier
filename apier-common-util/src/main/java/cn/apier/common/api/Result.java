package cn.apier.common.api;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by yanjunhua on 15/5/8.
 */
final public class Result<T extends Object> implements Serializable
{
    private boolean success;
    private ResultStatus status;
    private T data;

    private Result() {

    }

    public Result(boolean success, String code, String description, T data)
    {
        this.success = success;
        this.status = new ResultStatus(code, description);
        this.data = data;
    }

    public ResultStatus getStatus()
    {
        return status;
    }

    public T getData()
    {
        return data;
    }

    public static <T> Result<T> OK(T data)
    {
        return new Result(true, ResultStatus.statusOK, "", Objects.nonNull(data) ? data : "");
    }

    public static Result<Object> OK()
    {
        return OK(null);
    }

    public boolean isSuccess()
    {
        return success;
    }

    public static Result<Object> FAIL(String code, String description)
    {
        return new Result<>(false, code, description, "");
    }

    public static <T> Result<T> FAIL(T data)
    {
        return new Result(false, "", "", Objects.nonNull(data) ? data : "");
    }

    public static <T> Result<T> FAIL(String code, String description, T data)
    {
        return new Result(false, code, description, Objects.nonNull(data) ? data : "");
    }
}
