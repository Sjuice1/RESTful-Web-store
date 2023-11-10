package com.example.RESTftulSN.services;

import com.example.RESTftulSN.DTO.ReviewDTO;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Review;
import com.example.RESTftulSN.repositories.ReviewRepository;
import com.example.RESTftulSN.util.InvalidDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final ItemService itemService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,ModelMapper modelMapper, ItemService itemService) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.itemService = itemService;
    }

    @Transactional
    public void addReview(ReviewDTO reviewDTO) {
        Review review = dtoToModel(reviewDTO);
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteById(Long id) {
        Review review = getById(id);
        reviewRepository.delete(review);
    }

    public Review getById(Long id){
        Optional<Review> review = reviewRepository.findById(id);
        if(review.isEmpty()){
            throw new InvalidDataException("There's no review twith such id");
        }
        return review.get();
    }

    private Review dtoToModel(ReviewDTO reviewDTO) {
        Item item = itemService.getById(reviewDTO.getItem_id());
        Review review = modelMapper.map(reviewDTO,Review.class);
        review.setItem(item);
        review.setCreation_date(LocalDateTime.now());
        return review;

    }


}
