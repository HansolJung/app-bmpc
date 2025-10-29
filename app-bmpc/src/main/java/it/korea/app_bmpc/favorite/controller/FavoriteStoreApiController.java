package it.korea.app_bmpc.favorite.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.korea.app_bmpc.common.dto.ApiResponse;
import it.korea.app_bmpc.favorite.dto.FavoriteStoreDTO;
import it.korea.app_bmpc.favorite.service.FavoriteStoreService;
import it.korea.app_bmpc.user.dto.UserSecureDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteStoreApiController {

    private final FavoriteStoreService favoriteStoreService;

    /**
     * 찜 리스트 요청
     * @param pageable 페이징 객체
     * @param searchDTO 검색 객체
     * @param userId 사용자 아이디
     * @return
     * @throws Exception
     */
    @GetMapping("/favorite/user/{userId}")
    public ResponseEntity<?> getFavoriteStoreList(@PageableDefault(page = 0, size = 10, 
            sort = "createDate", direction = Direction.DESC) Pageable pageable,
            @PathVariable(name = "userId") String userId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!userId.equals(user.getUserId())) {   // 로그인 된 유저의 아이디와 PathVariable 로 넘어온 아이디가 일치하지 않을 경우...
            throw new RuntimeException("다른 사용자의 찜 목록은 볼 수 없습니다.");
        }
        
        Map<String, Object> resultMap = favoriteStoreService.getFavoriteStoreList(pageable, userId);

        return ResponseEntity.ok().body(ApiResponse.ok(resultMap));
    }

    /**
     * 찜 목록 존재 여부 확인 요청
     * @param userId 사용자 아이디
     * @param storeId 가게 아아디
     * @param user 로그인한 사용자
     * @return
     * @throws Exception
     */
    @GetMapping("/favorite/user/{userId}/store/{storeId}")
    public ResponseEntity<?> isFavoriteStore(@PathVariable(name = "userId") String userId,
            @PathVariable(name = "storeId") int storeId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!userId.equals(user.getUserId())) {   // 로그인 된 유저의 아이디와 PathVariable 로 넘어온 아이디가 일치하지 않을 경우...
            throw new RuntimeException("다른 사용자의 찜 목록 존재 여부는 확인할 수 없습니다.");
        }
        
        boolean isFavoriteStore = favoriteStoreService.isFavoriteStore(userId, storeId);

        return ResponseEntity.ok().body(ApiResponse.ok(isFavoriteStore));
    }

    /**
     * 찜 등록 요청
     * @param request
     * @param user 로그인한 사용자
     * @return
     * @throws Exception
     */
    @PostMapping("/favorite")
    public ResponseEntity<?> addFavoriteStore(@Valid @RequestBody FavoriteStoreDTO.Request request,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!request.getUserId().equals(user.getUserId())) {   // 로그인 된 유저의 아이디와 request 로 넘어온 아이디가 일치하지 않을 경우...
            throw new RuntimeException("다른 사용자의 찜 목록엔 등록할 수 없습니다.");
        }
    
        favoriteStoreService.addFavoriteStore(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 찜 목록에서 삭제 요청
     * @param userId 사용자 아이디
     * @param storeId 가게 아이디
     * @param user 로그인한 사용자
     * @return
     * @throws Exception
     */
    @DeleteMapping("/favorite/user/{userId}/store/{storeId}")
    public ResponseEntity<?> deleteFavoriteStore(@PathVariable(name = "userId") String userId,
            @PathVariable(name = "storeId") int storeId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {

        if (!userId.equals(user.getUserId())) {   // 로그인 된 유저의 아이디와 PathVariable 로 넘어온 아이디가 일치하지 않을 경우...
            throw new RuntimeException("다른 사용자의 찜 목록에서 삭제할 수 없습니다.");
        }
        
        favoriteStoreService.deleteFavoriteStore(userId, storeId);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }
}
