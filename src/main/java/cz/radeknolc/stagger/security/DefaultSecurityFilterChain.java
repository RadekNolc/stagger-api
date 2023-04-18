package cz.radeknolc.stagger.security;

import cz.radeknolc.stagger.config.WebSecurityConfig;
import cz.radeknolc.stagger.helper.auth.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public abstract class DefaultSecurityFilterChain {

    @Autowired
    protected AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    protected WebSecurityConfig webSecurityConfig;

    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors();
        httpSecurity.csrf().disable();

        httpSecurity.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers("/authenticate").permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                .anyRequest().authenticated()
        );

        httpSecurity.headers().frameOptions().disable();

        httpSecurity.authenticationProvider(webSecurityConfig.authenticationProvider());
        httpSecurity.addFilterBefore(webSecurityConfig.authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
