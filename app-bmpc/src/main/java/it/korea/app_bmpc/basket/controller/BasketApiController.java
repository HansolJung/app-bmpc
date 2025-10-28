package it.korea.app_bmpc.basket.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.korea.app_bmpc.basket.dto.BasketDTO;
import it.korea.app_bmpc.basket.service.BasketService;
import it.korea.app_bmpc.common.dto.ApiResponse;
import it.korea.app_bmpc.user.dto.UserSecureDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BasketApiController {

    private final BasketService basketService;

    /**
     * 나의 장바구니 가져오기
     * @param userId 사용자 아이디
     * @param user 로그인 된 사용자
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('USER')") // ROLE_USER 권한이 있어야 접근 가능
    @GetMapping("/basket/user/{userId}")
    public ResponseEntity<?> getBasket(@PathVariable(name = "userId") String userId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!userId.equals(user.getUserId())) {   // 로그인 된 유저의 아이디와 PathVariable 로 넘어온 아이디가 일치하지 않을 경우...
            throw new RuntimeException("다른 사용자의 장바구니는 볼 수 없습니다.");
        }

        Map<String, Object> resultMap = new HashMap<>();
        BasketDTO.Detail dto = basketService.getBasket(userId);
    
        resultMap.put("vo", dto);

        return ResponseEntity.ok().body(ApiResponse.ok(resultMap));
    }

    /**
     * 장바구니 전부 주문하기
     * @param userId 사용자 아이디
     * @param request
     * @param user 로그인한 사용자
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('USER')") // ROLE_USER 권한이 있어야 접근 가능
    @PostMapping("/basket/user/{userId}/order")
    public ResponseEntity<?> orderAllMenu(@PathVariable(name = "userId") String userId,
            @Valid @RequestBody BasketDTO.OrderRequest request,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!userId.equals(user.getUserId())) {
            throw new RuntimeException("다른 사용자의 장바구니를 주문할 수 없습니다.");
        }

        request.setUserId(userId);

        basketService.orderAllMenu(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 장바구니에 메뉴 추가하기
     * @param request
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('USER')") // ROLE_USER 권한이 있어야 접근 가능
    @PostMapping("/basket")
    public ResponseEntity<?> addMenu(@Valid @RequestBody BasketDTO.Request request,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {
    
        request.setUserId(user.getUserId());
        basketService.addMenu(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 장바구니 메뉴 삭제하기
     * @param userId 사용자 아이디
     * @param basketItemId 장바구니 항목 아이디
     * @param user
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('USER')") // ROLE_USER 권한이 있어야 접근 가능
    @DeleteMapping("/basket/user/{userId}/item/{basketItemId}")
    public ResponseEntity<?> deleteMenu(@PathVariable(name = "userId") String userId,
            @PathVariable(name = "basketItemId") int basketItemId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!userId.equals(user.getUserId())) {
            throw new RuntimeException("다른 사용자의 장바구니는 삭제할 수 없습니다.");
        }

        basketService.deleteMenu(userId, basketItemId);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }
    
    /**
     * 장바구니 메뉴 전부 삭제하기
     * @param userId 사용자 아이디
     * @param user 로그인한 사용자
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('USER')") // ROLE_USER 권한이 있어야 접근 가능
    @DeleteMapping("/basket/user/{userId}")
    public ResponseEntity<?> deleteAllMenu(@PathVariable(name = "userId") String userId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!userId.equals(user.getUserId())) {
            throw new RuntimeException("다른 사용자의 장바구니는 삭제할 수 없습니다.");
        }

        basketService.deleteAllMenu(userId);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }
}
