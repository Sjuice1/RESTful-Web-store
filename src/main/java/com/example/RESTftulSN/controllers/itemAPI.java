package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.services.ItemService;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.exceptions.ForbiddenAccessException;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/item")
public class itemAPI {
    private final ItemService itemService;
    @Autowired
    public itemAPI(ItemService itemService) {
        this.itemService = itemService;
    }
    ///////////////////////
    ////Get all items
    ///////////////////////
    @GetMapping
    public ResponseEntity<?> items(){
        return itemService.getAllItems();
    }
    ///////////////////////
    ////Get item by id
    ///////////////////////
    @GetMapping("{id}")
    public ResponseEntity<?> getItem(@PathVariable("id") Long id){
        return itemService.getItem(id);
    }
    ///////////////////////
    ////Get item reviews by item id
    ///////////////////////
    @GetMapping("{id}/reviews")
    public ResponseEntity<?> getItemReviews(@PathVariable("id") Long id){
        return itemService.getItemReviews(id);
    }
    ///////////////////////
    ////Add new item using JSON like ItemDTO
    ///////////////////////
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestBody @Valid ItemDTO itemDTO
            ,BindingResult bindingResult){
        return itemService.addItem(itemDTO,bindingResult);
    }
    ///////////////////////
    ////Delete item by id
    ///////////////////////
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> deleteItem(@PathVariable("id") Long id){
       return itemService.deleteItem(id);
    }
    ///////////////////////
    ////Update item by id
    ///////////////////////
    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> updateItem(@PathVariable("id") Long id,@RequestBody @Valid ItemDTO itemDTO,BindingResult bindingResult){
        return itemService.updateItemById(id,itemDTO,bindingResult);
    }

    @ExceptionHandler
    public ResponseEntity<?>  invalidDataHandler(InvalidDataException invalidDataException){
        return new ResponseEntity<>(new ErrorResponseEntity(invalidDataException.getMessage(), LocalDateTime.now()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?>  forbiddenAccessException(ForbiddenAccessException forbiddenAccessException){
        return new ResponseEntity<>(new ErrorResponseEntity(forbiddenAccessException.getMessage(), LocalDateTime.now()),HttpStatus.FORBIDDEN);
    }


}
