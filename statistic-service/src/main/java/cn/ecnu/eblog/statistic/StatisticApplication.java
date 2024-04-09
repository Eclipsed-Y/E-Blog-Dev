package cn.ecnu.eblog.statistic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StatisticApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticApplication.class);
    }
}
