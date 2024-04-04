package cn.ecnu.eblog.activity;

import cn.ecnu.eblog.common.feign.ArticleClient;
import cn.ecnu.eblog.common.feign.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients(clients = {ArticleClient.class})
public class ActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivityApplication.class, args);
    }
}
