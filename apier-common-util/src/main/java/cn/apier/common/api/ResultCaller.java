package cn.apier.common.api;

import java.util.function.Consumer;

/**
 * Created by yanjunhua on 15/12/28.
 */
public class ResultCaller
{
    public Result call(Consumer consumer, Object obj)
    {
        Result result = Result.OK();
        consumer.accept(obj);
        return result;
    }
}
