package com.icloud.my_portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.springdata.SpringDataDialect;


@SpringBootApplication
@Component
public class MyPortfolioApplication {

    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("SMTP");
        mailSender.setPort("PORTNUMBER");
        mailSender.setUsername("USERNAME");
        mailSender.setPassword("PASSWORD");
        return mailSender;
    }

    public static void main(String[] args) {
        SpringApplication.run(MyPortfolioApplication.class, args);
    }

}
