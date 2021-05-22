package com.icloud.my_portfolio.security.config;

import com.icloud.my_portfolio.resources.factory.UrlFilterMetadataFactoryBean;
import com.icloud.my_portfolio.resources.service.ResourcesService;
import com.icloud.my_portfolio.security.filter.PermitAllFilter;
import com.icloud.my_portfolio.security.metadata.UrlFilterInvocationSecurityMetadataSource;
import com.icloud.my_portfolio.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider authenticationProvider;
    private final ResourcesService resourcesService;
    private final UrlFilterMetadataFactoryBean urlFilterMetadataFactoryBean;

    private String[] permitAllList = {"/", "/about", "/js/**", "/account/register", "/vendor/**", "/login/**", "/css/**", "/img/**", "/webjars/**", "/api/**"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/account/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();

        http.addFilterBefore(permitAllFilter(), FilterSecurityInterceptor.class);

        http.exceptionHandling()
                .accessDeniedPage("/denied")
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/denied");
                });
    }

    @Bean
    public Filter permitAllFilter() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllList);

        permitAllFilter.setAuthenticationManager(authenticationManager());
        permitAllFilter.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        permitAllFilter.setAccessDecisionManager(getAccessDecisionManager());

        return permitAllFilter;
    }

    private AccessDecisionManager getAccessDecisionManager() {
        return new AffirmativeBased(getDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getDecisionVoters() {
        ArrayList<AccessDecisionVoter<?>> voters = new ArrayList<>();
        voters.add(getRoleHierarchy());

        return voters;
    }

    @Bean
    public AccessDecisionVoter<?> getRoleHierarchy() {
        return new RoleHierarchyVoter(getRoleHierarchyImpl());
    }
    @Bean
    public RoleHierarchy getRoleHierarchyImpl() {
        return new RoleHierarchyImpl();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetadataSource(urlFilterMetadataFactoryBean.getObject(),resourcesService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
