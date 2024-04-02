package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.model.Review;
import com.awbd.ecommerce.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;

    ModelMapper modelMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReviewDTO save(ReviewDTO reviewDTO) {
        Review savedReview = reviewRepository.save(modelMapper.map(reviewDTO, Review.class));
        return modelMapper.map(savedReview, ReviewDTO.class);
    }

    @Override
    public List<ReviewDTO> findAll() {
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO findById(Long l) {
        Optional<Review> reviewOptional = reviewRepository.findById(l);
        if (reviewOptional.isEmpty()) {
            throw new RuntimeException("Review not found!");
        }
        return modelMapper.map(reviewOptional.get(), ReviewDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public ReviewDTO update(Long id, ReviewDTO newReview) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);

        if(reviewOptional.isEmpty()) {
            throw new RuntimeException("Product not found!");
        }

        //TODO: edit the new review from the bd in a simpler way

        Review dbReview = reviewOptional.get();
        dbReview.setRating(newReview.getRating());
        dbReview.setContent(newReview.getContent());
        dbReview.setDate(newReview.getDate());
        dbReview.setUser(newReview.getUser());
        dbReview.setProduct(newReview.getProduct());

        return modelMapper.map(dbReview, ReviewDTO.class);
    }
}
