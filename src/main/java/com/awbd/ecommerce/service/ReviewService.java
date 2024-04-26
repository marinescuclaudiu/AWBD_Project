package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO save(ReviewDTO reviewDTO);
    List<ReviewDTO> findAll();

    ReviewDTO findById(Long id);
    void deleteById(Long id);

    ReviewDTO update(Long id, ReviewDTO reviewDTO);
}
