package cn.apier.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by yanjunhua on 2017/5/23.
 */
@SpringBootApplication
@EnableZuulProxy
@EnableConfigurationProperties
@EnableTransactionManagement
public class ApierAuthApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ApierAuthApplication.class, args);
    }
}
