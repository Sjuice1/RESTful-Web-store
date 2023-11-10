package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.UserDTO;
import com.example.RESTftulSN.DTO.UsersDTOForRegister;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.TokenGenerator;
import com.example.RESTftulSN.services.UserService;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.services.VerificationTokenService;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.InvalidDataException;
import com.example.RESTftulSN.util.UsernameAndEmailValidation;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class userAPI {
    private final UserService userService;
    private final UsernameAndEmailValidation usernameAndEmailValidation;
    private final BindingResultErrorCheck bindingResultErrorCheck;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public userAPI(UserService userService, UsernameAndEmailValidation usernameAndEmailValidation, BindingResultErrorCheck bindingResultErrorCheck, RabbitTemplate rabbitTemplate, TokenGenerator tokenGenerator, VerificationTokenService verificationTokenService, VerificationTokenService verificationTokenService1) {
        this.userService = userService;
        this.usernameAndEmailValidation = usernameAndEmailValidation;
        this.bindingResultErrorCheck = bindingResultErrorCheck;
        this.rabbitTemplate = rabbitTemplate;
    }

    ///////////////////////
    ////Get all users
    ///////////////////////
    @GetMapping
    public ResponseEntity<List<UserDTO>> users(){
        List<UserDTO> userList = userService.getAllUsersDTO();
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }
    ///////////////////////
    ////Get user by id
    ///////////////////////
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id){
        return new ResponseEntity<>(userService.getById(id).toDto(),HttpStatus.OK);
    }
    ///////////////////////
    ////Register new user
    ///////////////////////
    @PostMapping("/register")
    public HttpEntity<HttpStatus> registerUser(@RequestBody @Valid UsersDTOForRegister usersDTOForRegister, BindingResult bindingResult) {
        usernameAndEmailValidation.validate(new UserDTO(usersDTOForRegister.getUsername()
                ,usersDTOForRegister.getPassword()
                ,usersDTOForRegister.getEmail()), bindingResult);
        if (!usersDTOForRegister.getRepeatPassword().equals(usersDTOForRegister.getPassword())) {
            bindingResult.rejectValue("password", "", "Different Password");
        }
        bindingResultErrorCheck.check(bindingResult);
        Users user = userService.registerUser(usersDTOForRegister);
        rabbitTemplate.convertAndSend("Direct-Exchange","registration",user);
        return new HttpEntity<>(HttpStatus.OK);
    }
    ///////////////////////
    ////Delete user by id
    ///////////////////////
    @DeleteMapping("/delete/{id}")
    public HttpEntity<HttpStatus> deleteUser(@PathVariable("id") int id){
        userService.deleteById(id);
        return new HttpEntity<>(HttpStatus.OK);
    }
    ///////////////////////
    ////Update user by id
    ///////////////////////
    @PatchMapping("/update/{id}")
    public HttpEntity<HttpStatus> updateUser(@PathVariable("id") int id
            ,@RequestBody @Valid UserDTO userDTO
            ,BindingResult bindingResult){
        bindingResultErrorCheck.check(bindingResult);
        userService.updateUserById(id,userDTO);
        return new HttpEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseEntity> invalidDataHandler(InvalidDataException invalidDataException){
        return new ResponseEntity<>(new ErrorResponseEntity(invalidDataException.getMessage(), LocalDateTime.now()),HttpStatus.BAD_REQUEST);
    }

}
