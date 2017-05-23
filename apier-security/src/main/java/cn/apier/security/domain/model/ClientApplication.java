package cn.apier.security.domain.model;

import java.util.Date;

/**
 * Created by yanjunhua on 2017/5/23.
 * 客户端应用
 */
public class ClientApplication
{
    private String uid;
    private String name;
    private String appKey;
    private String secretKey;
    private Date createdAt;

    public ClientApplication(String uid, String name, String appKey, String secretKey, Date createdAt)
    {
        this.uid = uid;
        this.name = name;
        this.appKey = appKey;
        this.secretKey = secretKey;
        this.createdAt = createdAt;
    }
}
