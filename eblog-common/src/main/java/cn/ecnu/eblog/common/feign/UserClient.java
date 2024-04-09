package cn.ecnu.eblog.common.feign;

import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.UserInfoVO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.sound.midi.SoundbankResource;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping(value = "/user/{id}")
    Result<UserInfoVO> getUserInfoById(@PathVariable("id") Long id, @RequestHeader("userId") Long userId);

    @GetMapping(value = "/user/exists/{id}", headers = {"from=E"})
    Result<Boolean> existsUser(@PathVariable("id") Long id, @RequestHeader Long userId);
}
