package it.korea.app_bmpc.menu.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.korea.app_bmpc.common.dto.ApiResponse;
import it.korea.app_bmpc.menu.dto.MenuCategoryDTO;
import it.korea.app_bmpc.menu.dto.MenuDTO;
import it.korea.app_bmpc.menu.dto.MenuOptionDTO;
import it.korea.app_bmpc.menu.dto.MenuOptionGroupDTO;
import it.korea.app_bmpc.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MenuApiController {

    private final MenuService menuService;

    /**
     * 메뉴 상세정보 가져오기
     * @param menuId 메뉴 아이디
     * @return
     * @throws Exception
     */
    @GetMapping("/menu/{menuId}")
    public ResponseEntity<?> getMenu(@PathVariable(name = "menuId") int menuId) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        MenuDTO.Detail dto = menuService.getMenu(menuId);

        resultMap.put("vo", dto);

        return ResponseEntity.ok().body(ApiResponse.ok(resultMap));
    }

    /**
     * 메뉴 카테고리 등록하기
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/menu/category")
    public ResponseEntity<?> createMenuCategory(@Valid @RequestBody MenuCategoryDTO.Request request) throws Exception {
        
        menuService.createMenuCategory(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 카테고리 수정하기
     * @param request
     * @return
     * @throws Exception
     */
    @PutMapping("/menu/category")
    public ResponseEntity<?> updateMenuCategory(@Valid @RequestBody MenuCategoryDTO.Request request) throws Exception {
        
        menuService.updateMenuCategory(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 카테고리 삭제하기
     * @param menuCategoryId 메뉴 카테고리 아이디
     * @return
     * @throws Exception
     */
    @DeleteMapping("/menu/category/{menuCategoryId}")
    public ResponseEntity<?> deleteMenuCategory(@PathVariable(name = "menuCategoryId") int menuCategoryId) throws Exception {

        menuService.deleteMenuCategory(menuCategoryId);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 등록하기
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/menu")
    public ResponseEntity<?> createMenu(@Valid @ModelAttribute MenuDTO.Request request) throws Exception {
        
        menuService.createMenu(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 수정하기
     * @param request
     * @return
     * @throws Exception
     */
    @PutMapping("/menu")
    public ResponseEntity<?> updateMenu(@Valid @ModelAttribute MenuDTO.Request request) throws Exception {
        
        menuService.updateMenu(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 삭제하기
     * @param menuId 메뉴 아이디
     * @return
     * @throws Exception
     */
    @DeleteMapping("/menu/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable(name = "menuId") int menuId) throws Exception {

        menuService.deleteMenu(menuId);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 옵션 그룹 등록하기
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/menu/group")
    public ResponseEntity<?> createMenuOptionGroup(@Valid @RequestBody MenuOptionGroupDTO.Request request) throws Exception {
        
        menuService.createMenuOptionGroup(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 옵션 그룹 수정하기
     * @param request 
     * @return
     * @throws Exception
     */
    @PutMapping("/menu/group")
    public ResponseEntity<?> updateMenuOptionGroup(@Valid @RequestBody MenuOptionGroupDTO.Request request) throws Exception {

        menuService.updateMenuOptionGroup(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 옵션 그룹 삭제하기
     * @param menuOptGrpId 메뉴 옵션 그룹 아이디
     * @return
     * @throws Exception
     */
    @DeleteMapping("/menu/group/{menuOptGrpId}")
    public ResponseEntity<?> deleteMenuOptionGroup(@PathVariable(name = "menuOptGrpId") int menuOptGrpId) throws Exception {

        menuService.deleteMenuOptionGroup(menuOptGrpId);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 옵션 등록하기
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/menu/option")
    public ResponseEntity<?> createMenuOption(@Valid @RequestBody MenuOptionDTO.Request request) throws Exception {

        menuService.createMenuOption(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 옵션 수정하기
     * @param request
     * @return
     * @throws Exception
     */
    @PutMapping("/menu/option")
    public ResponseEntity<?> updateMenuOption(@Valid @RequestBody MenuOptionDTO.Request request) throws Exception {

        menuService.updateMenuOption(request);

        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }

    /**
     * 메뉴 옵션 삭제
     * @param menuOptId 메뉴 옵션 아이디
     * @return
     * @throws Exception
     */
    @DeleteMapping("/menu/option/{menuOptId}")
    public ResponseEntity<?> deleteMenuOption(@PathVariable(name = "menuOptId") int menuOptId) throws Exception {

        menuService.deleteMenuOption(menuOptId);
        
        return ResponseEntity.ok().body(ApiResponse.ok("OK"));
    }
}
