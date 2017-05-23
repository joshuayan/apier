package cn.apier.common.util.func;

import cn.apier.common.exception.CommonException;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created  on 16/9/10.
 * <p>
 * 如果checkFun满足则抛出exceptionFunc返回的异常
 */
public class LoadCheckExceptionFunc<T> implements CommonFunc
{
    private Supplier<T> loadFunc;
    private Predicate<T> checkFunc;

    private Function<T, ? extends RuntimeException> exceptionFunc;

    public LoadCheckExceptionFunc(Supplier<T> loadFunc)
    {
        this.loadFunc = loadFunc;
    }

    public LoadCheckExceptionFunc check(Predicate<T> checkFunc)
    {
        this.checkFunc = checkFunc;
        return this;
    }

    public LoadCheckExceptionFunc<T> ifNull()
    {
        check(t -> Objects.isNull(t));
        return this;
    }

    public LoadCheckExceptionFunc<T> ifNonNull()
    {
        check(t -> Objects.nonNull(t));
        return this;
    }


    public LoadCheckExceptionFunc<T> invalidException()
    {
        exception(t -> CommonException.invalidOperation());
        return this;
    }

    public LoadCheckExceptionFunc<T> nameDuplicatedException(String name)
    {
        exception(t -> CommonException.nameDuplicatedException(name));
        return this;
    }


    public LoadCheckExceptionFunc<T> codeDuplicatedException(String code)
    {
        exception(t -> CommonException.codeDuplicatedException(code));
        return this;
    }
    public LoadCheckExceptionFunc<T> parameterError()
    {
        exception(t -> CommonException.invalidOperation());
        return this;
    }




    public LoadCheckExceptionFunc exception(Function<T, ? extends RuntimeException> exceptionFunc)
    {
        this.exceptionFunc = exceptionFunc;
        return this;
    }

    @Override
    public void execute()
    {
        T obj = this.loadFunc.get();
        if (Objects.nonNull(checkFunc) && checkFunc.test(obj))
        {
            if (Objects.nonNull(exceptionFunc))
            {
                throw exceptionFunc.apply(obj);
            }
        }
    }
}
