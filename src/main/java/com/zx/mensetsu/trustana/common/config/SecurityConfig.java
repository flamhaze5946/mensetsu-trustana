package com.zx.mensetsu.trustana.common.config;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private static final String DEFAULT_ROLE = "USER";

    private final Set<String> users = Sets.newHashSet("Tom", "Jerry", "Takeshi");

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeHttpRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/logout").permitAll()
                    .antMatchers("/t/*").permitAll()
                    .antMatchers("/user/**").hasRole(DEFAULT_ROLE)
                .and().formLogin()
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/user/self")
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/user/self")
                .and().httpBasic()
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer =
                auth.inMemoryAuthentication().passwordEncoder(passwordEncoder);
        users.forEach(user -> configurer
                .withUser(user)
                .password(passwordEncoder.encode(user))
                .roles(DEFAULT_ROLE));
    }
}