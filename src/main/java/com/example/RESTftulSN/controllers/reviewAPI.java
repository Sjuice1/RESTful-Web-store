package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.ReviewDTO;
import com.example.RESTftulSN.enums.USER_ROLE;
import com.example.RESTftulSN.models.Review;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.security.UserDetailsImplementation;
import com.example.RESTftulSN.services.ReviewService;
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

@RestController
@RequestMapping("/api/review")
public class reviewAPI {
    private final ReviewService reviewService;
    private final BindingResultErrorCheck bindingResultErrorCheck;

    @Autowired
    public reviewAPI(ReviewService reviewService, BindingResultErrorCheck bindingResultErrorCheck) {
        this.reviewService = reviewService;
        this.bindingResultErrorCheck = bindingResultErrorCheck;
    }
    ///////////////////////
    ////Get review by id
    ///////////////////////
    @GetMapping("{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable("id") Long id){
        return new ResponseEntity<>(reviewService.getById(id).toDto(),HttpStatus.OK);
    }
    ///////////////////////
    ////Leave review for item
    ///////////////////////
    @PostMapping("/leave")
    public HttpEntity<HttpStatus> addReviewToItem(@RequestBody @Valid ReviewDTO reviewDTO
            ,BindingResult bindingResult){
        bindingResultErrorCheck.check(bindingResult);
        accessCheck(getCurrentUser().getUsers(),reviewDTO.getUser_id());
        reviewService.addReview(reviewDTO);
        return new HttpEntity<>(HttpStatus.OK);
    }
    ///////////////////////
    ////Delete review by id
    ///////////////////////
    @DeleteMapping("/delete/{id}")
    public HttpEntity<HttpStatus> deleteReview(@PathVariable("id") Long id){
        Review review = reviewService.getById(id);
        accessCheck(getCurrentUser().getUsers(),review.getUser().getId());
        reviewService.delete(review);
        return new HttpEntity<>(HttpStatus.OK);
    }

    ///////////////////////
    ////YOU CANT UPDATE REVIEW
    ////LEAVE NEW OR DELETE EXISTING ONE
    ///////////////////////

    @ExceptionHandler
    public ResponseEntity<ErrorResponseEntity> invalidDataHandler(InvalidDataException invalidDataException){
        return new ResponseEntity<>(new ErrorResponseEntity(invalidDataException.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponseEntity> forbiddenAccessException(ForbiddenAccessException forbiddenAccessException){
        return new ResponseEntity<>(new ErrorResponseEntity(forbiddenAccessException.getMessage(), LocalDateTime.now()),HttpStatus.FORBIDDEN);
    }

    private UserDetailsImplementation getCurrentUser(){
        return (UserDetailsImplementation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    private void accessCheck(Users user, Long id){
        if(!user.getUserRole().equals(USER_ROLE.ROLE_ADMIN) && !user.getUserRole().equals(USER_ROLE.ROLE_MODERATOR) &&  !user.getId().equals(id)) {
            throw new ForbiddenAccessException("You don't have permission for that");
        }
    }
}
