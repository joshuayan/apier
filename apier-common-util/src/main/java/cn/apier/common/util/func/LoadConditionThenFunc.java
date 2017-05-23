package cn.apier.common.util.func;

import cn.apier.common.exception.CommonException;
import cn.apier.common.util.ExecuteTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created  on 16/8/29.
 */
public class LoadConditionThenFunc<T> implements CommonFunc
{
    private Supplier<T> loadFunc;
    private List<Consumer<T>> consumers = new ArrayList<>();
    private Predicate<T> conditionalFunc;

    private Function<T, ? extends RuntimeException> exceptionFunc;

    public LoadConditionThenFunc(Supplier<T> loadFunc)
    {
        this.loadFunc = loadFunc;
    }

    public LoadConditionThenFunc<T> ifTrue(Predicate<T> booleanSupplier)
    {
        Objects.requireNonNull(booleanSupplier);
//        ExecuteTool.conditionalException(() -> Objects.nonNull(this.conditionalFunc), () -> CommonException.invalidOperation());
        this.conditionalFunc = booleanSupplier;
        return this;
    }

    public LoadConditionThenFunc<T> ifNonNull()
    {
        ifTrue(t -> Objects.nonNull(t));
        return this;
    }

    public LoadConditionThenFunc<T> ifNull()
    {
        ifTrue(t -> Objects.isNull(t));
        return this;
    }


    public LoadConditionThenFunc<T> invalidOperationException()
    {
        exception(t -> CommonException.invalidOperation());
        return this;
    }

    public LoadConditionThenFunc<T> exception(Function<T, ? extends RuntimeException> exceptionFunc)
    {
        Objects.requireNonNull(exceptionFunc);
        ExecuteTool.conditionalException(() -> Objects.nonNull(this.exceptionFunc), () -> CommonException.invalidOperation());
        this.exceptionFunc = exceptionFunc;
        return this;
    }


    public LoadConditionThenFunc<T> then(Consumer<T> processFunc)
    {
        Objects.requireNonNull(processFunc);
        this.consumers.add(processFunc);
        return this;
    }

    @Override
    public void execute()
    {
        T obj = this.loadFunc.get();
        if (Objects.isNull(conditionalFunc) || conditionalFunc.test(obj))
        {
//            ExecuteTool.ifTrue(() -> Objects.isNull(obj)).exception(() -> CommonException.NPE()).execute();
            if (Objects.nonNull(consumers))
            {
                consumers.forEach(tConsumer -> tConsumer.accept(obj));
            }
        } else
        {
            if (Objects.nonNull(exceptionFunc))
            {
                throw this.exceptionFunc.apply(obj);
            }
        }


    }
}
