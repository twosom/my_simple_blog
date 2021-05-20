package com.icloud.my_portfolio;


import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

import javax.persistence.EntityManager;



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
        mailSender.setHost("smtp.naver.com");
        mailSender.setPort(587);
        mailSender.setUsername("if0rever");
        mailSender.setPassword("F7D8NCRVXE9L");
        return mailSender;
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    public static void main(String[] args) {
        SpringApplication.run(MyPortfolioApplication.class, args);
    }



}
