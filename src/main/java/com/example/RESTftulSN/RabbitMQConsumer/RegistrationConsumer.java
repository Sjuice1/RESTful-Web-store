package com.example.RESTftulSN.RabbitMQConsumer;

import com.example.RESTftulSN.DTO.VerificationTokenDTO;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Order;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.TokenGenerator;
import com.example.RESTftulSN.services.VerificationTokenService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegistrationConsumer {
    private final TokenGenerator tokenGenerator;
    private final VerificationTokenService verificationTokenService;

    @Autowired
    public RegistrationConsumer(TokenGenerator tokenGenerator, VerificationTokenService verificationTokenService) {
        this.tokenGenerator = tokenGenerator;
        this.verificationTokenService = verificationTokenService;
    }
    ///////////////////
    ////DIRECT-EXCHANGE
    ///////////////////
    @RabbitListener(queues = "RegistrationToken")
    public void sendRegistrationToken(Users user){
        String tokenGeneration = tokenGenerator.generateToken();
        VerificationTokenDTO verificationTokenDTO = new VerificationTokenDTO(tokenGeneration
                ,LocalDateTime.now().plusMinutes(5));
        verificationTokenService.createToken(verificationTokenDTO,user);
        ////////////////////////////////////
        ///////SOME API FOR EMAIL SENDING
        ///////SEND JSON on USER.getEmail()
        ///////////////////////////////////
    }

    ///////////////////
    ////FANOUT-EXCHANGE
    ///////////////////
    @RabbitListener(queues = "OrderDetails")
    public void sendOrderDetails(Order order){
        String email = order.getUser().getEmail();
        ////////////////////////////////////
        ///////SOME API FOR EMAIL SENDING
        ///////SEND JSON ON email;
        ///////////////////////////////////
    }
    ///////////////////
    ////FANOUT-EXCHANGE
    ///////////////////
    @RabbitListener(queues = "SellerNotify")
    public void sendSellerNotify(Order order){
        Map<Users,Item> sellers = order.getItems()
                .stream()
                .collect(Collectors.toMap(Item::getSeller, item->item));
        for(Map.Entry<Users,Item> entry : sellers.entrySet()){
            ////////////////////////////////////
            ///////SOME API FOR EMAIL || NOTIFICATION SENDING
            ///////SEND JSON WITH entry
            ///////////////////////////////////
        }

    }

}
