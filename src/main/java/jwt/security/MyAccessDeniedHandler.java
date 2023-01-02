package jwt.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class MyAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return Mono.justOrEmpty(exchange)
                .map(ServerWebExchange::getResponse)
                .doOnNext(res -> {
                    res.setStatusCode(HttpStatus.FORBIDDEN);
                    System.out.println("you don't have access");

                    Map<String, String> myMap = Map.of("name", "rakshi");
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(myMap);
                        oos.close();
                        byte[] myMsg = baos.toByteArray();
                        res.writeAndFlushWith(Mono.just(Mono.just(res.bufferFactory().wrap(myMsg))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).then();
    }

}
