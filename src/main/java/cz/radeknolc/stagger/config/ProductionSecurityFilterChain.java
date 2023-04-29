package cz.radeknolc.stagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("production")
public class ProductionSecurityFilterChain extends GeneralSecurityFilterChain {

    @Bean
    @Override
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return super.filterChain(httpSecurity);
    }
}
