package jwt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ReactiveAuthenticationManager manager;
    private final AuthenticationConverter converter;
    private final ServerAccessDeniedHandler handler;

    @Bean
    SecurityWebFilterChain getSecurityFilterChain(ServerHttpSecurity security) {
        AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(this.manager);
        jwtFilter.setServerAuthenticationConverter(converter);
        return security
                .authorizeExchange()
                .pathMatchers("/jwt/login").permitAll()
                .pathMatchers("/api/admin/hey").hasRole("ADMIN")
                .anyExchange().authenticated()
                .and()
                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .formLogin()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(handler)
                .and()
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}
