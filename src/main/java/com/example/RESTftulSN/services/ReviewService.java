package com.example.RESTftulSN.services;

import com.example.RESTftulSN.DTO.ReviewDTO;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Review;
import com.example.RESTftulSN.repositories.ItemRepository;
import com.example.RESTftulSN.repositories.ReviewRepository;
import com.example.RESTftulSN.util.InvalidDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ItemService itemService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ItemRepository itemRepository, ItemService itemService) {
        this.reviewRepository = reviewRepository;
        this.itemService = itemService;
    }
    public Review getById(Long id){
        Optional<Review> review = reviewRepository.findById(id);
        if(review.isEmpty()){
            throw new InvalidDataException("There's no review twith such id");
        }
        return review.get();
    }

    public void addReview(ReviewDTO reviewDTO) {
        Review review = dtoToModel(reviewDTO);
        reviewRepository.save(review);
    }

    private Review dtoToModel(ReviewDTO reviewDTO) {
        Item item = itemService.getById(reviewDTO.getItem_id());
        ModelMapper modelMapper = new ModelMapper();
        Review review = modelMapper.map(reviewDTO,Review.class);
        review.setItem(item);
        review.setCreation_date(LocalDateTime.now());
        return review;

    }


}
