package com.example.RESTftulSN.security;

import com.example.RESTftulSN.enums.USER_ROLE;
import com.example.RESTftulSN.services.UsersDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityCFG {
    private final UsersDetailsServiceImplementation usersDetailsServiceImplementation;

    @Autowired
    public SecurityCFG(UsersDetailsServiceImplementation usersDetailsServiceImplementation) {
        this.usersDetailsServiceImplementation = usersDetailsServiceImplementation;
    }

    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(authorize->authorize.
                        requestMatchers("api/auth/generate/{id}")
                .hasRole("GUEST")
                .requestMatchers("/api/user","/api/user/delete/{id}","/api/order/delete/{id}")
                .hasRole("ADMIN")
                .requestMatchers("/api/order/{id}/status/{new_status}")
                .hasAnyRole("ADMIN","MODERATOR")
                .requestMatchers("/api/cart/{id}","/api/cart/put","api/cart/remove","/api/item/add","api/item/delete/{id}","api/item/update/{id}")
                .hasAnyRole("VERIFIED","MODERATOR","ADMIN")
                .anyRequest().permitAll())
                .build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(usersDetailsServiceImplementation);
        return daoAuthenticationProvider;
    }

    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
