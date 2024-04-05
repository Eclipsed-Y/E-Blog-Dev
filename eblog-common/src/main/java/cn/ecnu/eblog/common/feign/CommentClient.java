package cn.ecnu.eblog.common.feign;

import cn.ecnu.eblog.common.pojo.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("activity-service")
public interface CommentClient {
    @DeleteMapping(value = "/activity/comment/delete/{articleId}", headers = {"from=E"})
    Result<?> deleteByArticleId(@PathVariable("articleId") Long articleId, @RequestHeader Long userId);
}
