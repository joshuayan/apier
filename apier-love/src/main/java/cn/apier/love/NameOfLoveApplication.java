package cn.apier.love;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;

import javax.print.attribute.standard.MediaSize;

/**
 * Created by yanjunhua on 2017/5/24.
 */
@SpringCloudApplication
public class NameOfLoveApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(NameOfLoveApplication.class, args);
    }
}
