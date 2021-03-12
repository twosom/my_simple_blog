package com.icloud.my_portfolio.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//test를 위해서 csrf를 오프, 실사용시에는 키기.
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/posts/new").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/posts/{id}").permitAll()
                .antMatchers("/posts/**").hasRole("ADMIN")
                .antMatchers("/categories/**").hasRole("ADMIN")
                .antMatchers("/", "/about", "/js/**", "/account/register", "/vendor/**", "/login/**", "/css/**", "/img/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/account/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
