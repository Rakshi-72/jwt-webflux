package jwt.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MyManager implements ReactiveAuthenticationManager {

    private final ReactiveUserDetailsService service;
    private final JwtUtils utils;
    private String token;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        return Mono.justOrEmpty(authentication)
                .cast(BearerToken.class)
                .map(BearerToken::getCredentials)
                .doOnNext(t -> this.token = t)
                .map(utils::extractUsername)
                .flatMap(service::findByUsername)
                .filter(user -> utils.validateToken(token, user))
                .map(user -> new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

}
