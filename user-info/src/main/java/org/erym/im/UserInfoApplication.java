package org.erym.im;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author cjt
 * @date 2022/6/6 22:54
 */
@MapperScan("org.erym.im.mapper")
@EnableOpenApi
@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
public class UserInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserInfoApplication.class);
    }
}
