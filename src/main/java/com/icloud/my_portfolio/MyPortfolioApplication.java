package com.icloud.my_portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.dialect.springdata.SpringDataDialect;


@SpringBootApplication
@EnableCaching
public class MyPortfolioApplication {

    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

    public static void main(String[] args) {
        SpringApplication.run(MyPortfolioApplication.class, args);
    }

}
