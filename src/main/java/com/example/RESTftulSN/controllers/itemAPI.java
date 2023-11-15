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
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<?> getItem(@RequestBody @Validated ItemDTO.Request.Id itemDTO){
        return itemService.getItem(itemDTO.getId());
    }
    ///////////////////////
    ////Get item reviews by item id
    ///////////////////////
    @GetMapping("/reviews")
    public ResponseEntity<?> getItemReviews(@RequestBody @Validated ItemDTO.Request.Id itemDTO){
        return itemService.getItemReviews(itemDTO.getId());
    }
    ///////////////////////
    ////Add new item using JSON like ItemDTO
    ///////////////////////
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestBody @Valid ItemDTO.Request.Create itemDTO
            ,BindingResult bindingResult){
        return itemService.addItem(itemDTO,bindingResult);
    }
    ///////////////////////
    ////Delete item by id
    ///////////////////////
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> deleteItem(@RequestBody @Validated ItemDTO.Request.Id itemDTO){
       return itemService.deleteItem(itemDTO.getId());
    }
    ///////////////////////
    ////Update item by id
    ///////////////////////
    @PatchMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> updateItem(@RequestBody @Valid ItemDTO.Request.Create itemDTO,BindingResult bindingResult){
        return itemService.updateItemById(itemDTO,bindingResult);
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
