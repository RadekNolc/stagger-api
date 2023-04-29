package cz.radeknolc.stagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Profile("development")
public class DevelopmentSecurityFilterChain extends GeneralSecurityFilterChain {

    @Bean
    @Override
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
        );

        httpSecurity.headers().frameOptions().disable();
        return super.filterChain(httpSecurity);
    }
}
