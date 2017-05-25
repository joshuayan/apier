package cn.apier.auth.domain.model;

import cn.apier.common.util.DateTimeUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yanjunhua on 2017/5/23.
 * 客户端应用
 */
@Entity
public class ClientApplication
{
    @Id
    private String uid;
    private String name;
    private String appKey;
    private String secretKey;
    private Date createdAt = DateTimeUtil.now();

    private ClientApplication()
    {
    }

    public ClientApplication(String uid, String name, String appKey, String secretKey)
    {
        this.uid = uid;
        this.name = name;
        this.appKey = appKey;
        this.secretKey = secretKey;
    }

    public String getUid()
    {
        return uid;
    }

    public String getName()
    {
        return name;
    }

    public String getAppKey()
    {
        return appKey;
    }

    public String getSecretKey()
    {
        return secretKey;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }
}
