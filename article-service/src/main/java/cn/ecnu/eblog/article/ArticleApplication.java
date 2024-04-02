package cn.ecnu.eblog.article;

import cn.ecnu.eblog.common.feign.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {UserClient.class})  // 开启Feign功能，并指定要扫描的接口，{}代表数组，里面可以有多个，用逗号分开
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }
}
