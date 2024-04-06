package cn.ecnu.eblog.common.feign;

import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("article-service")
public interface ArticleClient {
    @GetMapping(value = "/article/exists/{id}", headers = {"from=E"})
    Result<Boolean> existsArticle(@PathVariable("id") Long id, @RequestHeader Long userId);
    @PutMapping(value = "/article/userInfo", headers = {"from=E"})
    Result<?> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO, @RequestHeader Long userId);
}
