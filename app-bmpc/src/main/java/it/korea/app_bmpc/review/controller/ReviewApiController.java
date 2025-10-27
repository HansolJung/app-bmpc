package it.korea.app_bmpc.review.controller;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.korea.app_bmpc.common.dto.ApiResponse;
import it.korea.app_bmpc.review.dto.ReviewDTO;
import it.korea.app_bmpc.review.service.ReviewService;
import it.korea.app_bmpc.user.dto.UserSecureDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewApiController {

    private final ReviewService reviewService;

    /**
     * 가게의 리뷰 리스트 요청
     * @param pageable 페이징 객체
     * @param storeId 가게 아이디
     * @return
     * @throws Exception
     */
    @GetMapping("/review/store/{storeId}")
    public ResponseEntity<?> getStoreReviewList(@PageableDefault(page = 0, size = 10, 
            sort = "updateDate", direction = Direction.DESC) Pageable pageable,
            @PathVariable(name = "storeId") int storeId) throws Exception {

        Map<String, Object> resultMap = reviewService.getStoreReviewList(pageable, storeId);

        return ResponseEntity.ok().body(ApiResponse.ok(resultMap));
    }

    /**
     * 사용자가 작성한 리뷰 리스트 요청
     * @param pageable 페이징 객체
     * @param userId 사용자 아이디
     * @return
     * @throws Exception
     */
    @GetMapping("/review/user/{userId}")
    public ResponseEntity<?> getUserReviewList(@PageableDefault(page = 0, size = 10, 
            sort = "updateDate", direction = Direction.DESC) Pageable pageable,
            @PathVariable(name = "userId") String userId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {
        
        if (!userId.equals(user.getUserId())) {   // 로그인 된 유저의 아이디와 PathVariable 로 넘어온 아이디가 일치하지 않을 경우...
            throw new RuntimeException("다른 사용자의 리뷰 리스트는 볼 수 없습니다.");
        }

        Map<String, Object> resultMap = reviewService.getUserReviewList(pageable, userId);

        return ResponseEntity.ok().body(ApiResponse.ok(resultMap));
    }

    /**
     * 리뷰 등록하기
     * @param request 
     * @param user 로그인한 사용자
     * @return
     * @throws Exception
     */
    @PostMapping("/review/user/{userId}")
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewDTO.Request request,
            @PathVariable(name = "userId") String userId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!userId.equals(user.getUserId())) {   // 로그인 된 유저의 아이디와 PathVariable 로 넘어온 아이디가 일치하지 않을 경우...
            throw new RuntimeException("다른 사용자의 리뷰는 등록할 수 없습니다.");
        }

        request.setUserId(user.getUserId());
        reviewService.createReview(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 리뷰 수정하기
     * @param request 
     * @param user 로그인한 사용자
     * @return
     * @throws Exception
     */
    @PutMapping("/review/user/{userId}")
    public ResponseEntity<?> updateReview(@Valid @RequestBody ReviewDTO.Request request,
            @PathVariable(name = "userId") String userId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!userId.equals(user.getUserId())) {   // 로그인 된 유저의 아이디와 PathVariable 로 넘어온 아이디가 일치하지 않을 경우...
            throw new RuntimeException("다른 사용자의 리뷰는 수정할 수 없습니다.");
        }

        request.setUserId(user.getUserId());
        reviewService.updateReview(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 리뷰 삭제하기
     * @param request 
     * @param user 로그인한 사용자
     * @return
     * @throws Exception
     */
    @DeleteMapping("/review/user/{userId}/review/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable(name = "userId") String userId,
            @PathVariable(name = "reviewId") int reviewId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!userId.equals(user.getUserId())) {   // 로그인 된 유저의 아이디와 PathVariable 로 넘어온 아이디가 일치하지 않을 경우...
            throw new RuntimeException("다른 사용자의 리뷰는 삭제할 수 없습니다.");
        }

        reviewService.deleteReview(userId, reviewId);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }
}
