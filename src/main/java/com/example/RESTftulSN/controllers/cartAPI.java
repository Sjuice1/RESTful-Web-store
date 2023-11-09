package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.CartDTO;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.services.ItemService;
import com.example.RESTftulSN.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/put")
    public HttpEntity<HttpStatus> putItemOnCart(@RequestBody @Valid CartDTO cartDTO
            ,BindingResult bindingResult){
        bindingResultErrorCheck.check(bindingResult);
        Users users = userService.getById(cartDTO.getUser_id());
        Item item = itemService.getById(cartDTO.getItem_id());
        userService.addItemToCart(users,item);
        return new HttpEntity<>(HttpStatus.OK);
    }
}
