package cn.apier.auth.application.service;

import cn.apier.auth.config.AuthConfig;
import cn.apier.auth.domain.model.ClientApplication;
import cn.apier.auth.domain.repository.ClientApplicationRepository;
import cn.apier.auth.filter.Constants;
import cn.apier.common.exception.CommonException;
import cn.apier.common.util.DateTimeUtil;
import cn.apier.common.util.ExecuteTool;
import cn.apier.common.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by yanjunhua on 2017/5/24.
 */
@Service
public class AuthService
{
    final private static Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private AuthConfig authConfig;
    @Autowired
    private ClientApplicationRepository clientApplicationRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TokenService tokenService;


    public String requestToken(String appKey, String ts, String sign)
    {
        ExecuteTool.conditionalException(() -> !checkTokenRequest(appKey, ts, sign), () -> CommonException.invalidOperation());

        String token = this.tokenService.buildToken(appKey, ts);

        redisTemplate.opsForValue().set(Constants.PREFIX_TOKEN_CLIENT_KEY + appKey, token);
        ClientApplication clientApplication = clientApplicationRepository.findByAppKey(appKey);

        Map<String, String> clientPropMap = new HashMap<>();
        clientPropMap.put("appKey", clientApplication.getAppKey());
        clientPropMap.put("secretKey", clientApplication.getSecretKey());
        redisTemplate.opsForHash().putAll(Constants.PREFIX_CLIENT_TOKEN_KEY + token, clientPropMap);

        return token;
    }

    private boolean checkTokenRequest(String appKey, String ts, String sign)
    {
        Date timestamp = DateTimeUtil.parse(ts, "yyyyMMddHHmmss");
        boolean result = checkParaNotEmpty(appKey, ts, sign) && checkTimestamp(timestamp) && checkSign(appKey, ts, sign);
        logger.debug("check token request? {}", result);
        return result;
    }


    private boolean checkParaNotEmpty(String appKey, String ts, String sign)
    {
        return Objects.nonNull(appKey) && Objects.nonNull(ts) && Objects.nonNull(sign);
    }

    private boolean checkTimestamp(Date timestamp)
    {
        long timeWindow = System.currentTimeMillis() - timestamp.getTime();
        boolean result = authConfig.getTimeWindowInMs() > timeWindow;
        logger.debug("check timestamp? {}", result);

        return result;
    }


    private boolean checkSign(String appKey, String ts, String sign)
    {
        boolean result = false;

        ClientApplication clientApplication = this.clientApplicationRepository.findByAppKey(appKey);
        if (Objects.nonNull(clientApplication))
        {
            String secretKey = clientApplication.getSecretKey();
            String sourceToSign = appKey + ts + secretKey;
            String signed = Utils.md5(sourceToSign);
            result = signed.toUpperCase().equals(sign);
        }

        logger.debug("check sign? {}", result);
        return result;
    }
}
