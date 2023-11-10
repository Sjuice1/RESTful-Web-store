package com.example.RESTftulSN.RabbitMQConsumer;

import com.example.RESTftulSN.DTO.VerificationTokenDTO;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.TokenGenerator;
import com.example.RESTftulSN.services.VerificationTokenService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationConsumer {
    private final TokenGenerator tokenGenerator;
    private final VerificationTokenService verificationTokenService;

    @Autowired
    public RegistrationConsumer(TokenGenerator tokenGenerator, VerificationTokenService verificationTokenService) {
        this.tokenGenerator = tokenGenerator;
        this.verificationTokenService = verificationTokenService;
    }

    @RabbitListener(queues = "RegistrationToken")
    public void consumeUser(Users user){
        String tokenGeneration = tokenGenerator.generateToken();
        VerificationTokenDTO verificationTokenDTO = new VerificationTokenDTO(tokenGeneration
                ,LocalDateTime.now().plusMinutes(5));
        verificationTokenService.createToken(verificationTokenDTO,user);
    }
}
