package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.model.Review;
import com.awbd.ecommerce.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public ReviewDTO findById(Long id) {
        Review review  = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found!"));

        return modelMapper.map(review,  ReviewDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review with id " + id + " not found!");
        }

        reviewRepository.deleteById(id);
    }

    @Override
    public ReviewDTO update(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found!"));

        BeanUtils.copyProperties(reviewDTO, review, BeanHelper.getNullPropertyNames(reviewDTO));

        reviewRepository.save(review);

        return modelMapper.map(review, ReviewDTO.class);
    }
}
