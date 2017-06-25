package cn.apier.auth.application.service;

import cn.apier.common.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by yanjunhua on 2017/5/24.
 */
@Service
public class TokenService
{
    final private static Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    public String buildToken(String appKey, String ts)
    {
        String token = UUIDUtil.commonUUIDWithoutSplit();
        LOGGER.debug("new token?[{}]", token);
        return token;
    }
}
