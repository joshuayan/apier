package cn.apier.common.util;

/**
 * Created  on 16/8/5.
 */
public class ResultContainer<T>
{
    private T result;

    public T getResult()
    {
        return result;
    }

    public void setResult(T result)
    {
        this.result = result;
    }
}
