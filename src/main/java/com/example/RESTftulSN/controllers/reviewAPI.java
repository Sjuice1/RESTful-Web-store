package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.ReviewDTO;
import com.example.RESTftulSN.enums.User.USER_ROLE;
import com.example.RESTftulSN.models.Review;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.security.UserDetailsImplementation;
import com.example.RESTftulSN.services.ReviewService;
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

@RestController
@RequestMapping("/api/review")
public class reviewAPI {
    private final ReviewService reviewService;

    @Autowired
    public reviewAPI(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    ///////////////////////
    ////Get review by id
    ///////////////////////
    @GetMapping("{id}")
    public ResponseEntity<?> getReview(@PathVariable("id") Long id){
        return reviewService.getReviewById(id);
    }
    ///////////////////////
    ////Leave review for item
    ///////////////////////

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    @PostMapping("/leave")
    public ResponseEntity<?> addReviewToItem(@RequestBody @Valid ReviewDTO reviewDTO
            ,BindingResult bindingResult){
        return reviewService.addReview(reviewDTO,bindingResult);
    }
    ///////////////////////
    ////Delete review by id
    ///////////////////////
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable("id") Long id){
        return reviewService.delete(id);
    }

    ///////////////////////
    ////YOU CANT UPDATE REVIEW
    ////LEAVE NEW OR DELETE EXISTING ONE
    ///////////////////////

    @ExceptionHandler
    public ResponseEntity<?> invalidDataHandler(InvalidDataException invalidDataException){
        return new ResponseEntity<>(new ErrorResponseEntity(invalidDataException.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<?> forbiddenAccessException(ForbiddenAccessException forbiddenAccessException){
        return new ResponseEntity<>(new ErrorResponseEntity(forbiddenAccessException.getMessage(), LocalDateTime.now()),HttpStatus.FORBIDDEN);
    }

}
