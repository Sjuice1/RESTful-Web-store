package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.enums.USER_ROLE;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.UserDetailsImplementation;
import com.example.RESTftulSN.services.UserService;
import com.example.RESTftulSN.services.VerificationTokenService;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.ForbiddenAccessException;
import com.example.RESTftulSN.util.InvalidDataException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class authAPI {
    private final VerificationTokenService verificationTokenService;
    private final RabbitTemplate rabbitTemplate;
    private final UserService userService;

    @Autowired
    public authAPI(VerificationTokenService verificationTokenService, RabbitTemplate rabbitTemplate, UserService userService) {
        this.verificationTokenService = verificationTokenService;
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
    }
    ///////////////////////
    ////Token authentication
    ///////////////////////
    @GetMapping("/{token}")
    public HttpEntity<HttpStatus> authenticateUser(@PathVariable("token") String token){
        if(verificationTokenService.checkToken(token)){
            return new HttpEntity<>(HttpStatus.OK);
        }
        return new HttpEntity<>(HttpStatus.NOT_FOUND);
    }
    ///////////////////////
    ////Generate new token for user by user id
    ///////////////////////
    @PostMapping("/generate/{id}")
    public HttpEntity<HttpStatus> generateNewToken(@PathVariable("id") Long id){
        Users user = userService.getById(id);
        accessCheck(getCurrentUser().getUsers(),user.getId());
        rabbitTemplate.convertAndSend("Direct-Exchange","registration",user);
        return new HttpEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseEntity> invalidDataHandler(InvalidDataException invalidDataException){
        return new ResponseEntity<>(new ErrorResponseEntity(invalidDataException.getMessage(), LocalDateTime.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponseEntity> forbiddenAccessException(ForbiddenAccessException forbiddenAccessException){
        return new ResponseEntity<>(new ErrorResponseEntity(forbiddenAccessException.getMessage(), LocalDateTime.now()),HttpStatus.FORBIDDEN);
    }

    private UserDetailsImplementation getCurrentUser(){
        return (UserDetailsImplementation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    private void accessCheck(Users user,Long id){
        if(!user.getUserRole().equals(USER_ROLE.ROLE_ADMIN) && !user.getUserRole().equals(USER_ROLE.ROLE_MODERATOR) &&  !user.getId().equals(id)) {
            throw new ForbiddenAccessException("You don't have permission for that");
        }
    }
}
