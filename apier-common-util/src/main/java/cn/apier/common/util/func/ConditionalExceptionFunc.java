package cn.apier.common.util.func;


import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Created  on 16/8/29.
 */
public class ConditionalExceptionFunc implements CommonFunc
{
    private BooleanSupplier conditionFunc;
    private Supplier<? extends RuntimeException> exceptionFunc;

    public ConditionalExceptionFunc(BooleanSupplier booleanSupplier)
    {
        this.conditionFunc = booleanSupplier;
    }

    public ConditionalExceptionFunc exception(Supplier<? extends RuntimeException> exceptionFunc)
    {
        Objects.requireNonNull(exceptionFunc);
        this.exceptionFunc = exceptionFunc;
        return this;
    }

    public void execute()
    {
        boolean conditional = this.conditionFunc.getAsBoolean();
        if (conditional && Objects.nonNull(exceptionFunc))
        {
            throw exceptionFunc.get();
        }
    }
}
