package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.UserDTO.UserDTO;
import com.example.RESTftulSN.DTO.UserDTO.UsersDTOForRegister;
import com.example.RESTftulSN.enums.User.USER_ROLE;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.TokenGenerator;
import com.example.RESTftulSN.security.UserDetailsImplementation;
import com.example.RESTftulSN.services.UserService;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.services.VerificationTokenService;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.exceptions.ForbiddenAccessException;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import com.example.RESTftulSN.util.validations.UsernameAndEmailValidation;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class userAPI {
    private final UserService userService;

    @Autowired
    public userAPI(UserService userService) {
        this.userService = userService;
    }

    ///////////////////////
    ////Get all users
    ///////////////////////
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(){
        return userService.getAllUsersDTO();
    }
    ///////////////////////
    ////Get user by id
    ///////////////////////
    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }
    ///////////////////////
    ////Register new user
    ///////////////////////
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UsersDTOForRegister usersDTOForRegister, BindingResult bindingResult) {
        return userService.registerUser(usersDTOForRegister,bindingResult);
    }
    ///////////////////////
    ////Delete user by id
    ///////////////////////
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        return userService.deleteById(id);

    }
    ///////////////////////
    ////Update user by id
    ///////////////////////
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id,@RequestBody @Valid UserDTO userDTO,BindingResult bindingResult){

        return userService.updateUserById(id,userDTO,bindingResult);
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
