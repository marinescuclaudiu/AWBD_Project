package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.model.Review;
import com.awbd.ecommerce.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;

    ModelMapper modelMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReviewDTO save(ReviewDTO reviewDTO) {
        log.info("Saving review: {}, rating: {}", reviewDTO.getContent(), reviewDTO.getRating());
        Review savedReview = reviewRepository.save(modelMapper.map(reviewDTO, Review.class));

        log.info("Review saved: {}, rating: {}", reviewDTO.getContent(), reviewDTO.getRating());
        return modelMapper.map(savedReview, ReviewDTO.class);
    }

    @Override
    public List<ReviewDTO> findAll() {
        log.info("Fetching all reviews");
        List<Review> reviews = reviewRepository.findAll();

        log.info("Found {} reviews", reviews.size());
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO findById(Long id) {
        log.info("Fetching review by ID: {}", id);
        Optional<Review> review  = reviewRepository.findById(id);

        if(review.isEmpty()){
            log.error("Review with id {} not found!", id);
            throw new ResourceNotFoundException("Review with id " + id + " not found!");
        }

        log.info("Review found: {}, rating: {}", review.get().getContent(), review.get().getRating());
        return modelMapper.map(review.get(),  ReviewDTO.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting review by ID: {}", id);

        if (!reviewRepository.existsById(id)) {
            log.error("Review with id {} not found", id);
            throw new ResourceNotFoundException("Review with id " + id + " not found!");
        }

        reviewRepository.deleteById(id);
        log.info("Review deleted successfully");
    }

    @Transactional
    @Override
    public ReviewDTO update(Long id, ReviewDTO reviewDTO) {
        log.info("Updating review with ID: {}", id);
        Optional<Review> review = reviewRepository.findById(id);

        if(review.isEmpty()){
            log.error("Review with id {} not found", id);
            throw new ResourceNotFoundException("Review with id " + id + " not found!");
        }

        BeanUtils.copyProperties(reviewDTO, review, BeanHelper.getNullPropertyNames(reviewDTO));

        reviewRepository.save(review.get());

        log.info("Review updated: {}, rating: {}", review.get().getContent(), review.get().getRating());
        return modelMapper.map(review.get(), ReviewDTO.class);
    }
}
