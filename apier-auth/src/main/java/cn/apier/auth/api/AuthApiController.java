package cn.apier.auth.api;

import cn.apier.auth.domain.model.ClientApplication;
import cn.apier.common.util.ExecuteTool;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/token")
    public ResponseEntity<String> token(String appKey, String ts, String sign)
    {
        ExecuteTool.checkStringParameterNonNullOrEmpty(appKey);
        ExecuteTool.checkStringParameterNonNullOrEmpty(ts);
        ExecuteTool.checkStringParameterNonNullOrEmpty(sign);

        return ResponseEntity.ok("ok");
    }
}
