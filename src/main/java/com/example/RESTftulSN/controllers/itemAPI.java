package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.DTO.ReviewDTO;
import com.example.RESTftulSN.enums.USER_ROLE;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.UserDetailsImplementation;
import com.example.RESTftulSN.services.ItemService;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.ForbiddenAccessException;
import com.example.RESTftulSN.util.InvalidDataException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/item")
public class itemAPI {
    private final ItemService itemService;
    private final BindingResultErrorCheck bindingResultErrorCheck;

    @Autowired
    public itemAPI(ItemService itemService, BindingResultErrorCheck bindingResultErrorCheck) {
        this.itemService = itemService;
        this.bindingResultErrorCheck = bindingResultErrorCheck;
    }
    ///////////////////////
    ////Get all items
    ///////////////////////
    @GetMapping
    public ResponseEntity<List<ItemDTO>> items(){
        List<ItemDTO> itemList = itemService.getAllItems().stream()
                .map(item->new ItemDTO(item.getName()
                ,item.getDescription()
                ,item.getPrice()
                ,item.getStateOfItem()
                ,item.getItemCount()
                ,item.getImgSource()
                ,item.getItemCount())).toList();
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }
    ///////////////////////
    ////Get item by id
    ///////////////////////
    @GetMapping("{id}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable("id") Long id){
        return new ResponseEntity<>(itemService.getById(id).toDto(),HttpStatus.OK);
    }
    ///////////////////////
    ////Get item reviews
    ///////////////////////
    @GetMapping("{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> getItemReviews(@PathVariable("id") Long id){
        Item item = itemService.getById(id);
        List<ReviewDTO> reviewDTOS = item.getReviews().stream()
                .map(review -> new ReviewDTO(review.getMark(), review.getDescription(), item.getId()))
                .toList();
        return new ResponseEntity<>(reviewDTOS,HttpStatus.OK);
    }
    ///////////////////////
    ////Add new item
    ///////////////////////
    @PostMapping("/add")
    public HttpEntity<HttpStatus> addItem(@RequestBody @Valid ItemDTO itemDTO
            ,BindingResult bindingResult){
        bindingResultErrorCheck.check(bindingResult);
        accessCheck(getCurrentUser().getUsers(),itemDTO.getSellerId());
        itemService.addItem(itemDTO);
        return new HttpEntity<>(HttpStatus.OK);
    }
    ///////////////////////
    ////Delete item by id
    ///////////////////////
    @DeleteMapping("/delete/{id}")
    public HttpEntity<HttpStatus> deleteItem(@PathVariable("id") Long id){
        Item item = itemService.getById(id);
        accessCheck(getCurrentUser().getUsers(),item.getSeller().getId());
        itemService.deleteItem(item);
        return new HttpEntity<>(HttpStatus.OK);
    }
    ///////////////////////
    ////Update item by id
    ///////////////////////
    @PatchMapping("/update/{id}")
    public HttpEntity<HttpStatus> updateItem(@PathVariable("id") Long id
            ,@RequestBody @Valid ItemDTO itemDTO
            ,BindingResult bindingResult){
        bindingResultErrorCheck.check(bindingResult);
        accessCheck(getCurrentUser().getUsers(),itemDTO.getSellerId());
        itemService.updateItemById(id,itemDTO);
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
    private void accessCheck(Users user, Long id){
        if(!user.getUserRole().equals(USER_ROLE.ROLE_ADMIN) && user.getUserRole().equals(USER_ROLE.ROLE_MODERATOR) && !user.getId().equals(id)) {
            throw new ForbiddenAccessException("You don't have permission for that");
        }
    }

}
