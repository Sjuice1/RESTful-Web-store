package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.CartDTO;
import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.enums.User.USER_ROLE;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.security.UserDetailsImplementation;
import com.example.RESTftulSN.services.ItemService;
import com.example.RESTftulSN.services.UserService;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.exceptions.ForbiddenAccessException;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import jakarta.validation.Valid;
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
@RequestMapping("/api/cart")
public class cartAPI {
    private final UserService userService;
    private final ItemService itemService;
    private final BindingResultErrorCheck bindingResultErrorCheck;

    @Autowired
    public cartAPI(UserService userService, ItemService itemService, BindingResultErrorCheck bindingResultErrorCheck) {
        this.userService = userService;
        this.itemService = itemService;
        this.bindingResultErrorCheck = bindingResultErrorCheck;
    }
    ///////////////////////
    ////Get user cart by user id
    ///////////////////////
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    ResponseEntity<List<ItemDTO>> cartOfUser(@PathVariable("id") Long id){
        Users user = userService.getById(id);
        accessCheck(getCurrentUser().getUsers(),id);
        List<ItemDTO> list = user.getItems().stream().
                map(item -> new ItemDTO(item.getName(),item.getDescription()
                        ,item.getPrice(),item.getStateOfItem(),
                         item.getItemCount()
                        ,item.getImgSource()
                        ,item.getSeller().getId())).toList();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    ///////////////////////
    ////Put new item in user cart
    ///////////////////////
    @PostMapping("/put")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public HttpEntity<HttpStatus> putItemOnCart(@RequestBody @Valid CartDTO cartDTO
            ,BindingResult bindingResult){
        bindingResultErrorCheck.check(bindingResult);
        Users user = userService.getById(cartDTO.getUserId());
        accessCheck(getCurrentUser().getUsers(),cartDTO.getUserId());
        Item item = itemService.getById(cartDTO.getItemId());
        userService.addItemToCart(user,item);
        return new HttpEntity<>(HttpStatus.OK);
    }
    ///////////////////////
    ////Remove item from user cart
    ///////////////////////
    @DeleteMapping("/remove")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public HttpEntity<HttpStatus> removeItemFromCart(@RequestBody @Valid CartDTO cartDTO,BindingResult bindingResult){
        bindingResultErrorCheck.check(bindingResult);
        Users user = userService.getById(cartDTO.getUserId());
        accessCheck(getCurrentUser().getUsers(),cartDTO.getUserId());
        Item item = itemService.getById(cartDTO.getItemId());
        userService.removeItemFromCart(user,item);
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
        return (UserDetailsImplementation)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    private void accessCheck(Users user,Long id){
        if(!user.getUserRole().equals(USER_ROLE.ROLE_ADMIN) && !user.getUserRole().equals(USER_ROLE.ROLE_MODERATOR) &&  !user.getId().equals(id)) {
            throw new ForbiddenAccessException("You don't have permission for that");
        }
    }
}
