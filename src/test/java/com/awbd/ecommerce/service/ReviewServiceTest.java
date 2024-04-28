package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.model.Review;
import com.awbd.ecommerce.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private ReviewServiceImpl reviewService;
    @Mock
    ModelMapper modelMapper;

    @Test
    public void save_Success() {
        // Arrange
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setContent("Test review");
        reviewDTO.setRating(5);

        Review reviewToSave = new Review();
        reviewToSave.setContent("Test review");
        reviewToSave.setRating(5);

        Review savedReview = new Review();
        savedReview.setId(1L);
        savedReview.setContent("Test review");
        savedReview.setRating(5);

        when(modelMapper.map(reviewDTO, Review.class)).thenReturn(reviewToSave);

        when(reviewRepository.save(reviewToSave)).thenReturn(savedReview);

        when(modelMapper.map(savedReview, ReviewDTO.class)).thenReturn(reviewDTO);

        // Act
        ReviewDTO savedDTO = reviewService.save(reviewDTO);

        // Assert
        assertNotNull(savedDTO);
        assertEquals(reviewDTO.getContent(), savedDTO.getContent());
        assertEquals(reviewDTO.getRating(), savedDTO.getRating());

        verify(reviewRepository, times(1)).save(reviewToSave);

        verify(modelMapper, times(1)).map(reviewDTO, Review.class);
        verify(modelMapper, times(1)).map(savedReview, ReviewDTO.class);

        verifyNoMoreInteractions(reviewRepository, modelMapper);
    }

    @Test
    void findAll_Success() {
        // Arrange
        Review review1 = new Review();
        review1.setId(1L);
        review1.setContent("Great product");
        review1.setRating(5);

        Review review2 = new Review();
        review2.setId(2L);
        review2.setContent("Could be better");
        review2.setRating(3);

        List<Review> reviewList = Arrays.asList(review1, review2);

        when(reviewRepository.findAll()).thenReturn(reviewList);

        when(modelMapper.map(review1, ReviewDTO.class)).thenReturn(new ReviewDTO(1L, 5, "Great product"));
        when(modelMapper.map(review2, ReviewDTO.class)).thenReturn(new ReviewDTO(2L, 3, "Could be better"));

        // Act
        List<ReviewDTO> reviewDTOList = reviewService.findAll();

        // Assert
        assertEquals(2, reviewDTOList.size());
        assertEquals("Great product", reviewDTOList.get(0).getContent());
        assertEquals(5, reviewDTOList.get(0).getRating());
        assertEquals("Could be better", reviewDTOList.get(1).getContent());
        assertEquals(3, reviewDTOList.get(1).getRating());

        verify(reviewRepository, times(1)).findAll();

        verify(modelMapper, times(1)).map(review1, ReviewDTO.class);
        verify(modelMapper, times(1)).map(review2, ReviewDTO.class);

        verifyNoMoreInteractions(reviewRepository, modelMapper);
    }

    @Test
    void findById_Success() {
        // Arrange
        Long reviewId = 1L;
        String reviewContent = "Great product";
        int reviewRating = 5;

        Review review = new Review();
        review.setId(reviewId);
        review.setContent(reviewContent);
        review.setRating(reviewRating);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(modelMapper.map(review, ReviewDTO.class)).thenReturn(new ReviewDTO(review.getId(),  review.getRating(), review.getContent()));

        // Act
        ReviewDTO foundReviewDTO = reviewService.findById(reviewId);

        // Assert
        assertNotNull(foundReviewDTO);
        assertEquals(reviewContent, foundReviewDTO.getContent());
        assertEquals(reviewRating, foundReviewDTO.getRating());

        verify(reviewRepository, times(1)).findById(reviewId);

        verify(modelMapper, times(1)).map(review, ReviewDTO.class);

        verifyNoMoreInteractions(reviewRepository, modelMapper);
    }

    @Test
    void findById_ReviewNotFound() {
        // Arrange
        Long reviewId = 1L;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.findById(reviewId);
        });

        verify(reviewRepository, times(1)).findById(reviewId);
        verifyNoMoreInteractions(reviewRepository, modelMapper);
    }

    @Test
    public void deleteById_Success() {
        // Arrange
        Long reviewId = 1L;

        when(reviewRepository.existsById(reviewId)).thenReturn(true);

        // Act
        reviewService.deleteById(reviewId);

        // Assert
        verify(reviewRepository, times(1)).deleteById(reviewId);
        verify(reviewRepository, times(1)).existsById(reviewId);
        verifyNoMoreInteractions(reviewRepository, modelMapper);
    }

    @Test
    public void deleteById_ReviewNotFound() {
        // Arrange
        Long reviewId = 1L;

        when(reviewRepository.existsById(reviewId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.deleteById(reviewId);
        });

        verify(reviewRepository, times(1)).existsById(reviewId);
        verifyNoMoreInteractions(reviewRepository, modelMapper);
    }

    @Test
    public void update_Success() {
        // Arrange
        Long reviewId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setContent("Updated review");
        reviewDTO.setRating(4);

        Review review = new Review();
        review.setId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(modelMapper.map(review, ReviewDTO.class)).thenReturn(new ReviewDTO(review.getId(), review.getRating(), review.getContent()));

        // Act
        ReviewDTO updatedReviewDTO = reviewService.update(reviewId, reviewDTO);

        // Assert
        verify(reviewRepository, times(1)).save(review);
        verify(modelMapper, times(1)).map(review, ReviewDTO.class);
        verify(reviewRepository, times(1)).findById(reviewId);
        verifyNoMoreInteractions(reviewRepository, modelMapper);
    }

    @Test
    void update_ReviewNotFound() {
        // Arrange
        Long reviewId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setContent("Updated review");
        reviewDTO.setRating(4);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.update(reviewId, reviewDTO);
        });

        verify(reviewRepository, times(1)).findById(reviewId);
        verifyNoMoreInteractions(reviewRepository, modelMapper);
    }
}
