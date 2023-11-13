package com.example.RESTftulSN.services;

import com.example.RESTftulSN.DTO.ReviewDTO;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Review;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.repositories.ReviewRepository;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final BindingResultErrorCheck bindingResultErrorCheck;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ModelMapper modelMapper, ItemService itemService, UserService userService, BindingResultErrorCheck bindingResultErrorCheck) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.itemService = itemService;
        this.userService = userService;
        this.bindingResultErrorCheck = bindingResultErrorCheck;
    }

    @Transactional
    public ResponseEntity<?> addReview(ReviewDTO reviewDTO, BindingResult bindingResult) {
        bindingResultErrorCheck.check(bindingResult);
        Review review = dtoToModel(reviewDTO);
        reviewRepository.save(review);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> delete(Long id) {
        Review review = getById(id);
        reviewRepository.delete(review);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Review getById(Long id){
        Optional<Review> review = reviewRepository.findById(id);
        if(review.isEmpty()){
            throw new InvalidDataException("There's no review twith such id");
        }
        return review.get();
    }

    public ResponseEntity<?> getReviewById(Long id) {
        Review review = getById(id);
        return new ResponseEntity<>(review.toDto(),HttpStatus.OK);
    }
    private Review dtoToModel(ReviewDTO reviewDTO) {
        Users user = userService.getById(reviewDTO.getUser_id());
        Item item = itemService.getById(reviewDTO.getItem_id());
        Review review = modelMapper.map(reviewDTO,Review.class);
        review.setItem(item);
        review.setCreation_date(LocalDateTime.now());
        review.setUser(user);
        return review;

    }

}
