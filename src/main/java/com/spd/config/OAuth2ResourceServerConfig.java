package com.spd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous().and()
                .authorizeRequests()
                .antMatchers("/**/*.js",
                        "/login", "/**/*.js",
                        "/swagger*",
                        "/swagger-resources/**",
                        "/api-docs",
                        "/fonts/*",
                        "/v2/api-docs**",
                        "/webjars/**",
                        "/images/*.*")
                            .permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/users")
                    .permitAll()
                .anyRequest().authenticated();
    }

}