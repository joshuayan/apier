package cn.apier.common.api;

import java.io.Serializable;

/**
 * Created by yanjunhua on 15/5/8.
 */
final public class ResultStatus implements Serializable
{
    final public static String statusOK = "0";
    private String code;
    private String description;

    public ResultStatus(String code, String description)
    {
        this.code = code;
        this.description = description;
    }

    public String getCode()
    {
        return code;
    }

    public String getDescription()
    {
        return description;
    }
}
