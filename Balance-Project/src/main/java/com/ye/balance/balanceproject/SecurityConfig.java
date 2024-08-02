package com.ye.balance.balanceproject;

import com.ye.balance.balanceproject.model.domain.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    // for password encoding
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // for request mapping authorization
    @Bean
    SecurityFilterChain httpSecurityFilterChain(HttpSecurity http) throws Exception {

       http.formLogin( form -> form.loginPage("/signin").defaultSuccessUrl("/"));

       http.logout(logout -> logout.logoutUrl("/signout").logoutSuccessUrl("/"));

       http.authorizeHttpRequests(auth -> auth
               .requestMatchers("/signin","/signup","/").permitAll()
               .requestMatchers("/user/**").
                hasAnyAuthority(User.Role.Member.name() , User.Role.Admin.name())
               .requestMatchers("/admin/**").hasAuthority(User.Role.Admin.name())
               .anyRequest().authenticated()
       );

       http.exceptionHandling(customizer ->customizer.accessDeniedPage("/denied-page"));

        return http.build();
    }

    //for log message ( signin and also error at sign in time )
    @Bean
    AuthenticationEventPublisher authenticationEventPublisher(){
        return new DefaultAuthenticationEventPublisher() ;
    }

    // for logout message
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }

}
