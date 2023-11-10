package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.ReviewDTO;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.services.ReviewService;
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

    @GetMapping("{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable("id") Long id){
        return new ResponseEntity<>(reviewService.getById(id).toDto(),HttpStatus.OK);
    }
    @PostMapping("/leave")
    public HttpEntity<HttpStatus> addReviewToItem(@RequestBody @Valid ReviewDTO reviewDTO
            ,BindingResult bindingResult){
        bindingResultErrorCheck.check(bindingResult);
        reviewService.addReview(reviewDTO);
        return new HttpEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public HttpEntity<HttpStatus> deleteItem(@PathVariable("id") Long id){
        reviewService.deleteById(id);
        return new HttpEntity<>(HttpStatus.OK);
    }


    ///////////////////////////////////////////
    ///////////////////////////////////////////
    /////// NO UPDATE FOR REVIEW,YOU CAN DELETE OR CREATE NEW ON TOP OF PREVIOUS
    ///////////////////////////////////////////
    ///////////////////////////////////////////

    @ExceptionHandler
    public ResponseEntity<ErrorResponseEntity> invalidDataHandler(InvalidDataException invalidDataException){
        return new ResponseEntity<>(new ErrorResponseEntity(invalidDataException.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
