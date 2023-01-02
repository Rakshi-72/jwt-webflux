package jwt.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jwt.models.LoginRequest;
import jwt.models.User;
import jwt.repositories.UserRepository;
import jwt.security.JwtUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtLogin {

    private final JwtUtils utils;
    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private String token;

    @PostMapping("/login")
    public String getLoginToken(@RequestBody LoginRequest request) {

        User user = repository.findByEmail(request.getUsername());
        if (encoder.matches(request.getPassword(), user.getPassword())) {
            this.token = utils.generateToken(request.getUsername());
            System.out.println(token);
        }
        return token;
    }

}
