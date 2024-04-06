package cn.ecnu.eblog.user;

import cn.ecnu.eblog.common.feign.ArticleClient;
import cn.ecnu.eblog.common.feign.CommentClient;
import cn.ecnu.eblog.common.feign.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {ArticleClient.class, CommentClient.class})
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
