//package com.example.watchex.config;
//
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
//
//@Configuration
//@AllArgsConstructor
    //@EnableWebSecurity
    //public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/admin/auth/**", "/bower_components/**", "/component/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated().and()
//                .formLogin((form) -> form
//                        .loginPage("/admin/auth/login")
//                        .defaultSuccessUrl("/admin/users", true)
//                        .permitAll()
//                )
//                .logout(LogoutConfigurer::permitAll);
//
//    }
//
//
//}