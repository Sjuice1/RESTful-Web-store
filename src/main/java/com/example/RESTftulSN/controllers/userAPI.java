package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.UserDTO;
import com.example.RESTftulSN.services.UserService;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.exceptions.ForbiddenAccessException;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(){
        return userService.getAllUsersDTO();
    }
    ///////////////////////
    ////Get user by id
    ///////////////////////
    @GetMapping()
    public ResponseEntity<?> getUserById(@RequestBody @Validated UserDTO.Request.Id userDTO){
        return userService.getUserById(userDTO.getId());
    }
    ///////////////////////
    ////Register new user
    ///////////////////////
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO.Request.Register userDTO, BindingResult bindingResult) {
        return userService.registerUser(userDTO,bindingResult);
    }
    ///////////////////////
    ////Delete user by id
    ///////////////////////
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody @Validated UserDTO.Request.Id userDTO){
        return userService.deleteById(userDTO.getId());
    }
    ///////////////////////
    ////Update user by id
    ///////////////////////
    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO.Request.Update userDTO,BindingResult bindingResult){
        return userService.updateUserById(userDTO,bindingResult);
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
