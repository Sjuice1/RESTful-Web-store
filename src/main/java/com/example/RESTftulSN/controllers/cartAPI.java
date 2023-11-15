package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.CartDTO;

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
@RequestMapping("/api/cart")
public class cartAPI {
    private final UserService userService;

    @Autowired
    public cartAPI(UserService userService) {
        this.userService = userService;

    }
    ///////////////////////
    ////Get user cart by user id
    ///////////////////////
    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    ResponseEntity<?> cartOfUser(@RequestBody @Validated CartDTO.Request.UserCart cartDTO){
        return userService.getCartOfUser(cartDTO.getUserId());
    }
    ///////////////////////
    ////Put new item in user cart
    ///////////////////////
    @PostMapping("/put")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> putItemOnCart(@RequestBody @Valid CartDTO.Request.Create cartDTO,BindingResult bindingResult){
        return userService.addItemToCart(cartDTO,bindingResult);

    }
    ///////////////////////
    ////Remove item from user cart
    ///////////////////////
    @DeleteMapping("/remove")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> removeItemFromCart(@RequestBody @Valid CartDTO.Request.Create cartDTO,BindingResult bindingResult){
        return userService.removeItemFromCart(cartDTO,bindingResult);

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
