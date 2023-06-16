package cz.radeknolc.stagger.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static cz.radeknolc.stagger.model.response.ServerResponseMessage.AUTH_USER_VERIFY_ERROR;

@Component
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Unauthorized: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, String.valueOf(AUTH_USER_VERIFY_ERROR));
    }
}
