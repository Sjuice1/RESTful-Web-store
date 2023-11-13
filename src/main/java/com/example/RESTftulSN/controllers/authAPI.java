package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.enums.User.USER_ROLE;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.UserDetailsImplementation;
import com.example.RESTftulSN.services.UserService;
import com.example.RESTftulSN.services.VerificationTokenService;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.exceptions.ForbiddenAccessException;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class authAPI {
    private final VerificationTokenService verificationTokenService;
    private final UserService userService;

    @Autowired
    public authAPI(VerificationTokenService verificationTokenService,UserService userService) {
        this.verificationTokenService = verificationTokenService;
        this.userService = userService;
    }
    ///////////////////////
    ////Token authentication
    ///////////////////////
    @GetMapping("/{token}")
    public ResponseEntity<?> authenticateUser(@PathVariable("token") String token){
        return verificationTokenService.checkToken(token);

    }
    ///////////////////////
    ////Generate new token for user by user id
    ///////////////////////
    @PostMapping("/generate/{id}")
    @PreAuthorize("hasAnyRole('ROLE_GUEST')")
    public ResponseEntity<?> generateNewToken(@PathVariable("id") Long id){
        return userService.generateNewToken(id);
    }

    @ExceptionHandler
    public ResponseEntity<?> invalidDataHandler(InvalidDataException invalidDataException){
        return new ResponseEntity<>(new ErrorResponseEntity(invalidDataException.getMessage(), LocalDateTime.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<?> forbiddenAccessException(ForbiddenAccessException forbiddenAccessException){
        return new ResponseEntity<>(new ErrorResponseEntity(forbiddenAccessException.getMessage(), LocalDateTime.now()),HttpStatus.FORBIDDEN);
    }


}
