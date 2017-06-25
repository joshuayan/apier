package cn.apier.auth.api;

import cn.apier.auth.application.service.AuthService;
import cn.apier.common.api.Result;
import cn.apier.common.util.DateTimeUtil;
import cn.apier.common.util.ExecuteTool;
import cn.apier.common.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yanjunhua on 2017/5/24.
 */
@RestController
@RequestMapping("/auth")
public class AuthApiController
{

    @Autowired
    private AuthService authService;

    @GetMapping("/token")
    public Result<String> token(String appKey, String ts, String sign)
    {
        String secretKey="sf23wd#s21";
       String mockAppKey="2345qw";
       String mockTs= DateTimeUtil.formatDate(DateTimeUtil.now(),"yyyyMMddHHmmss");
       String mockSign= Utils.md5(mockAppKey+mockTs+secretKey).toUpperCase();

       appKey=mockAppKey;
       ts=mockTs;
       sign=mockSign;

        ExecuteTool.checkStringParameterNonNullOrEmpty(appKey);
        ExecuteTool.checkStringParameterNonNullOrEmpty(ts);
        ExecuteTool.checkStringParameterNonNullOrEmpty(sign);



        return ExecuteTool.executeQueryWithTry(() -> this.authService.requestToken(mockAppKey, mockTs, mockSign));
    }
}
