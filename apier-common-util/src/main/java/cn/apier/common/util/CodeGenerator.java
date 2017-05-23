package cn.apier.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class CodeGenerator
{

    public static String newSaleOrderCode()
    {
        return commonCode("SO");
    }

    /**
     * 出库单
     **/
    public static String newODOCode()
    {
        return commonCode("DO");
    }

    /**
     * 入库单
     **/
    public static String newWVCode()
    {
        return commonCode("WV");
    }

    public static String commonIdWithDate()
    {
        Date current = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = dateFormat.format(current);
        String postfix = Utils.generateMixNumber(5);
        return dateStr + postfix;
    }

    public static String commonId()
    {
        String msStr = System.currentTimeMillis() + "";
        String postfix = Utils.generateMixNumber(19 - msStr.length());
        return msStr + postfix;
    }

    /**
     * 生成通用编码
     *
     * @param prefix
     * @return 前缀yyyyMMdd-8位随机字符，例如  PO20160708-XEWs23rt
     */
    public static String commonCode(String prefix)
    {
        return prefix + DateTimeUtil.formatDate(DateTimeUtil.now(), "yyyyMMdd") + "-" + UUIDUtil.generateShortUuid();
    }
}
