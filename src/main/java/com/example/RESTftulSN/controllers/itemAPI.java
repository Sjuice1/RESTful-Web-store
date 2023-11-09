package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.DTO.UserDTO;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.services.ItemService;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.InvalidDataException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<Item>> users(){
        List<Item> itemList = itemService.getAllItems();
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<ItemDTO> getUser(@PathVariable("id") int id){
        return new ResponseEntity<>(itemService.getById(id).toDto(),HttpStatus.OK);
    }
    @PostMapping("/add")
    public HttpEntity<HttpStatus> addItem(@RequestBody @Valid ItemDTO itemDTO
            ,BindingResult bindingResult){
        bindingResultErrorCheck.check(bindingResult);
        itemService.addItem(itemDTO);
        return new HttpEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public HttpEntity<HttpStatus> deleteUser(@PathVariable("id") int id){
        itemService.deleteById(id);
        return new HttpEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/update/{id}")
    public HttpEntity<HttpStatus> updateUser(@PathVariable("id") int id
            ,@RequestBody @Valid ItemDTO itemDTO
            ,BindingResult bindingResult){
        bindingResultErrorCheck.check(bindingResult);
        itemService.updateUserById(id,itemDTO);
        return new HttpEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseEntity> invalidDataHandler(InvalidDataException invalidDataException){
        return new ResponseEntity<>(new ErrorResponseEntity(invalidDataException.getMessage(), LocalDateTime.now()),HttpStatus.BAD_REQUEST);
    }

}
