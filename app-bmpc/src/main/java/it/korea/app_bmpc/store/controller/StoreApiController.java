package it.korea.app_bmpc.store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.korea.app_bmpc.common.dto.ApiResponse;
import it.korea.app_bmpc.store.dto.CategoryDTO;
import it.korea.app_bmpc.store.dto.StoreDTO;
import it.korea.app_bmpc.store.dto.StoreSearchDTO;
import it.korea.app_bmpc.store.service.StoreService;
import it.korea.app_bmpc.user.dto.UserSecureDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StoreApiController {

    private final StoreService storeService;
    
    /**
     * 가게 카테고리 리스트 가져오기
     * @return
     * @throws Exception
     */
    @GetMapping("/store/category")
    public ResponseEntity<?> getCategoryList() throws Exception {

        List<CategoryDTO> categoryList = storeService.getCategoryList();

        return ResponseEntity.ok().body(ApiResponse.ok(categoryList));
    }

    /**
     * 가게 리스트 가져오기
     * @param pageable 페이징 객체
     * @param searchDTO 검색 객체
     * @return
     * @throws Exception
     */
    @GetMapping("/store")
    public ResponseEntity<?> getStoreList(@PageableDefault(page = 0, size = 10, 
            sort = "updateDate", direction = Direction.DESC) Pageable pageable,
            StoreSearchDTO searchDTO) throws Exception {

        Map<String, Object> resultMap = storeService.getStoreList(pageable, searchDTO);

        return ResponseEntity.ok().body(ApiResponse.ok(resultMap));
    }

    /**
     * 가게 상세정보 가져오기
     * @param storeId 가게 아이디
     * @return
     * @throws Exception
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> getStore(@PathVariable(name = "storeId") int storeId) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        StoreDTO.Detail dto = storeService.getStore(storeId);

        resultMap.put("vo", dto);

        return ResponseEntity.ok().body(ApiResponse.ok(resultMap));
    }

    /**
     * 가게 등록하기
     * @param request
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('OWNER')") // ROLE_OWNER 권한이 있어야 접근 가능
    @PostMapping("/store")
    public ResponseEntity<?> createStore(@Valid @ModelAttribute StoreDTO.Request request, 
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {
        
        storeService.createStore(request, user.getUserId());

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 가게 수정하기
     * @param request
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('OWNER')") // ROLE_OWNER 권한이 있어야 접근 가능
    @PutMapping("/store")
    public ResponseEntity<?> updateStore(@Valid @ModelAttribute StoreDTO.Request request,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {
        
        storeService.updateStore(request, user.getUserId());

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 가게 삭제하기
     * @param storeId 가게 아이디
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('OWNER')") // ROLE_OWNER 권한이 있어야 접근 가능
    @DeleteMapping("/store/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable(name = "storeId") int storeId,
            @AuthenticationPrincipal UserSecureDTO user) throws Exception {
 
        storeService.deleteStore(storeId, user.getUserId());

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

}
