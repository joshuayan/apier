package cn.apier.common.page;

import com.github.pagehelper.PageInfo;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Created by yanjunhua on 2016/10/27.
 */
public class CommonPageInfo
{
    private String queryUrl;
    private PageInfo pageInfo;

    public CommonPageInfo(String queryUrl, PageInfo pageInfo)
    {
        this.queryUrl = queryUrl;
        this.pageInfo = Objects.requireNonNull(pageInfo);
    }

    public String getQueryUrl()
    {
        return queryUrl;
    }

    public int pageCount()
    {
        return this.pageInfo.getPages();
    }

    public int currentPageNum()
    {
        return this.pageInfo.getPageNum();
    }

    public PageInfo getPageInfo()
    {
        return pageInfo;
    }

    public Map<String, String> navigatePageUrls()
    {
        Map<String, String> result = new TreeMap<>();
        for (int page : this.pageInfo.getNavigatepageNums())
        {
            String pageUrl = buildPageUrl(page, this.pageInfo.getPageSize());
            result.put(page+"", pageUrl);
        }

        return result;
    }

    public String firstPageUrl()
    {
        return buildPageUrl(this.pageInfo.getFirstPage(), this.pageInfo.getPageSize());
    }

    public String prePageUrl()
    {
        return buildPageUrl(this.pageInfo.getPrePage(), this.pageInfo.getPageSize());
    }

    public String nextPageUrl()
    {
        int nextPage = this.pageInfo.getNextPage();
        int pageSize = this.pageInfo.getPageSize();
        return buildPageUrl(nextPage, pageSize);
    }

    public String lastPageUrl()
    {
        return buildPageUrl(this.pageInfo.getLastPage(), this.pageInfo.getPageSize());
    }

    private String buildPageUrl(int pageNum, int pageSize)
    {
        StringBuilder result = new StringBuilder(this.queryUrl);
        if (!result.toString().contains("?"))
        {
            result.append("?");
        }

        if (result.toString().contains("="))
        {
            result.append("&");
        }

        result.append("pageNo=" + pageNum).append("&pageSize=" + pageSize);

        return result.toString();
    }


}
