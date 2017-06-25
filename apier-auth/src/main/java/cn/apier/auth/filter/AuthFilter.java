package cn.apier.auth.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

/**
 * Created by yanjunhua on 2017/5/24.
 */
@Component
public class AuthFilter extends ZuulFilter
{
    final public static Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private ProxyRequestHelper helper;

    @Override
    public String filterType()
    {
        return "pre";
    }

    @Override
    public int filterOrder()
    {
        return -1;
    }

    @Override
    public boolean shouldFilter()
    {
//        String requestUrl = this.helper.buildZuulRequestURI(RequestContext.getCurrentContext().getRequest());
//        LOGGER.debug("request url [{}]", requestUrl);
//
//        boolean skipped = false;
//
//        if (Objects.nonNull(requestUrl) && !requestUrl.isEmpty() && requestUrl.contains("/auth/token"))
//        {
//            skipped = true;
//        }
//
//        LOGGER.debug("skipped? [{}]", skipped);
//        return !skipped;
        return true;
    }

    @Override
    public Object run()
    {
        RequestContext requestContext=RequestContext.getCurrentContext();
        if (!(checkParameterExist() && checkSign()))
        {
//            requestContext.setSendZuulResponse(false);
        }
        return null;
    }


    private boolean checkParameterExist()
    {
        MultiValueMap<String, String> paras = this.helper.buildZuulRequestQueryParams(RequestContext.getCurrentContext().getRequest());
        boolean result = paras.containsKey(Constants.PARA_ACCESS_TOKEN) && paras.containsKey(Constants.PARA_TIMESTAMP) && paras.containsKey(Constants.PARA_SIGNATURE);
        LOGGER.debug("check parameter exist ? [{}]", result);
        return result;
    }

    private boolean checkSign()
    {
        MultiValueMap<String, String> paras = this.helper.buildZuulRequestQueryParams(RequestContext.getCurrentContext().getRequest());

        String sign = paras.getFirst(Constants.PARA_SIGNATURE);
        String ts = paras.getFirst(Constants.PARA_TIMESTAMP);
        String token = paras.getFirst(Constants.PARA_ACCESS_TOKEN);


        return false;
    }
}
