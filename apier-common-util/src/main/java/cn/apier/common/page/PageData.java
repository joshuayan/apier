package cn.apier.common.page;

/**
 * Created by yanjunhua on 2016/10/14.
 */
public class PageData<T>
{
    private CommonPageInfo pageInfo;
    private T data;

    public PageData( T data,CommonPageInfo pageInfo)
    {
        this.pageInfo = pageInfo;
        this.data = data;
    }

    public CommonPageInfo getPageInfo()
    {
        return pageInfo;
    }

    public T getData()
    {
        return data;
    }
}
