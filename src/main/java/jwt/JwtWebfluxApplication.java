package jwt;

import jwt.models.Role;
import jwt.models.User;
import jwt.repositories.RoleRepository;
import jwt.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class JwtWebfluxApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public JwtWebfluxApplication(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(JwtWebfluxApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Role role = new Role(1, "ROLE_USER");
        roleRepository.save(role);

        User user = new User(1, "rakshi", "rakshi@test.com", encoder.encode("1234"));
        user.getRoles().add(roleRepository.findById(1).get());

        User savedUser = userRepository.save(user);
        System.out.println(savedUser);
    }
}
