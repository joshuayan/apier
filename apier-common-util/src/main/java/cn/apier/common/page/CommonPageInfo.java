package cn.apier.common.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.pagehelper.PageInfo;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Created by yanjunhua on 2016/10/27.
 */
public class CommonPageInfo {
    private String queryUrl;
    private PageInfo pageInfo;
    final public static String PARAMETER_PAGE_NO = "pageNo";
    final public static String PARAMETER_PAGE_SIZE = "pageSize";

    public CommonPageInfo(String queryUrl, PageInfo pageInfo) {
        this.queryUrl = queryUrl;
        this.pageInfo = Objects.requireNonNull(pageInfo);
    }

    @JsonProperty
    public String getQueryUrl() {
        return queryUrl;
    }

    @JsonProperty
    public int pageCount() {
        return this.pageInfo.getPages();
    }

    @JsonProperty
    public int currentPageNum() {
        return this.pageInfo.getPageNum();
    }

//    public PageInfo getPageInfo()
//    {
//        return pageInfo;
//    }

    @JsonProperty
    public Map<String, String> navigatePageUrls() {
        Map<String, String> result = new TreeMap<>();
        for (int page : this.pageInfo.getNavigatepageNums()) {
            String pageUrl = buildPageUrl(page, this.pageInfo.getPageSize());
            result.put(page + "", pageUrl);
        }

        return result;
    }

    @JsonProperty
    public boolean hasNextPage() {
        return this.pageInfo.isHasNextPage();
    }

    @JsonProperty
    public boolean hasPrePage() {
        return this.pageInfo.isHasPreviousPage();
    }

    @JsonProperty
    public String firstPageUrl() {
        return buildPageUrl(this.pageInfo.getFirstPage(), this.pageInfo.getPageSize());
    }

    @JsonProperty
    public String prePageUrl() {
        return this.hasPrePage() ? buildPageUrl(this.pageInfo.getPrePage(), this.pageInfo.getPageSize()) : "";
    }

    @JsonProperty
    public String nextPageUrl() {
        int nextPage = this.pageInfo.getNextPage();
        int pageSize = this.pageInfo.getPageSize();
        return buildPageUrl(nextPage, pageSize);
    }

    @JsonProperty
    public String lastPageUrl() {
        return buildPageUrl(this.pageInfo.getLastPage(), this.pageInfo.getPageSize());
    }

    private String buildPageUrl(int pageNum, int pageSize) {
        StringBuilder result = new StringBuilder(this.queryUrl);
        if (pageNum <= 0) pageNum = 1;
        if (pageSize > this.pageCount()) pageSize = this.pageCount();
        if (!result.toString().contains("?")) {
            result.append("?");
        }

        if (result.toString().contains("=")) {
            result.append("&");
        }

        result.append("pageNo=" + pageNum).append("&pageSize=" + pageSize);

        return result.toString();
    }


}
