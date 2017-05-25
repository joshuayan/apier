package cn.apier.love.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yanjunhua on 2017/5/24.
 */
@RestController
@RequestMapping("/love")
public class LoveApiController
{
    final private static Logger LOGGER = LoggerFactory.getLogger(LoveApiController.class);

    @GetMapping("/index")
    public ResponseEntity<String> index()
    {
        LOGGER.debug("index");
        return ResponseEntity.ok("index ok");
    }


    @GetMapping("/next")
    public ResponseEntity<String> next()
    {
        LOGGER.debug("next");
        return ResponseEntity.ok("next ok");
    }
}
