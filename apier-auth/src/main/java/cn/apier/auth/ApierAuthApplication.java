package cn.apier.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by yanjunhua on 2017/5/23.
 */
@SpringBootApplication
@EnableZuulProxy
public class ApierAuthApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ApierAuthApplication.class, args);
    }
}
