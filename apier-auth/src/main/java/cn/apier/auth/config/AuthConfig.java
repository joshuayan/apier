package cn.apier.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yanjunhua on 2017/5/25.
 */
@ConfigurationProperties(prefix = "apier.auth")
@Configuration
public class AuthConfig
{
    private long timeWindowInMs;


    public long getTimeWindowInMs()
    {
        return timeWindowInMs;
    }

    public void setTimeWindowInMs(long timeWindowInMs)
    {
        this.timeWindowInMs = timeWindowInMs;
    }
}
