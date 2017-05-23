package cn.apier.common.page;

import java.util.Objects;

/**
 * Created by yanjunhua on 2016/10/14.
 */
public class PageRequest
{
    private int pageNo;
    private int pageSize;

    private PageRequest()
    {
    }

    public PageRequest(int pageNo, int pageSize)
    {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public static PageRequest validateAndBuild(Integer pageNo, Integer pageSize)
    {
        if (Objects.isNull(pageNo))
        {
            pageNo = 1;
        }
        if (Objects.isNull(pageSize))
        {
            pageSize = 10;
        }

        if (pageNo < 1)
        {
            pageNo = 1;
        }

        if (pageSize < 1)
        {
            pageSize = 10;
        }

        return new PageRequest(pageNo, pageSize);
    }
}
