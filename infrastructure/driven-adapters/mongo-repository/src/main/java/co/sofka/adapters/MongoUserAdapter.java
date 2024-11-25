package co.sofka.adapters;

import co.sofka.AuthenticationRequest;
import co.sofka.AuthenticationResponse;
import co.sofka.JwtService;
import co.sofka.UserRequest;
import co.sofka.data.CustomerDocument;
import co.sofka.data.UserDocument;
import co.sofka.jwt.UserRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Repository
public class MongoUserAdapter implements UserRepository {

    private final ReactiveMongoTemplate template;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public MongoUserAdapter(ReactiveMongoTemplate template, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.template = template;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<AuthenticationResponse> register(UserRequest userRequest) {
        CustomerDocument customerDocument=new CustomerDocument();
        customerDocument.setName(userRequest.getFirstname()+" "+userRequest.getLastname());

        UserDocument user = new UserDocument.Builder()
                .setFirstName(userRequest.getFirstname())
                .setLastName(userRequest.getLastname())
                .setEmail(userRequest.getEmail())
                .setPassword(passwordEncoder.encode(userRequest.getPassword()))
                .setCustomer(customerDocument)
                .setRole(userRequest.getRole())
                .build();


        return template.save(user)
                .map(savedUser -> new AuthenticationResponse.Builder()
                        .id(savedUser.getId())
                        .build())
                .doOnError(e -> {
                    System.out.println("Error during user registration: " + e.getMessage());
                });
    }

    @Override
    public Mono<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        Query query = new Query(Criteria.where("email").is(authenticationRequest.getEmail()));

        return template.findOne(query, UserDocument.class)
                .flatMap(userDocument -> {

                    if (userDocument == null) {
                        return Mono.error(new RuntimeException("User not found"));
                    }

                    if (!passwordEncoder.matches(authenticationRequest.getPassword(), userDocument.getPassword())) {
                        return Mono.error(new RuntimeException("Wrong password"));
                    }

                    List<String> roles = List.of(userDocument.getRole().toString());

                    String jwtToken = jwtService.generateToken(userDocument,
                            Map.of("roles", String.join(",", roles)));

                    return Mono.just(new AuthenticationResponse
                            .Builder()
                            .token(jwtToken)
                            .id(userDocument.getId())
                            .build());
                });
    }
}
