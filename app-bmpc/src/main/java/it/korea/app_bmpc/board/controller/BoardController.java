package it.korea.app_bmpc.board.controller;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.korea.app_bmpc.admin.service.AdminUserService;
import it.korea.app_bmpc.common.dto.ApiResponse;
import it.korea.app_bmpc.sms.service.SmsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {

    private final AdminUserService adminUserService;
    private final SmsService smsService;

    @GetMapping("/board")
    public ResponseEntity<?> getBoardList(@PageableDefault(size = 10, page = 0,
        sort = "userId", direction = Direction.DESC) Pageable pageable) throws Exception {

        Map<String, Object> resultMap = adminUserService.getUserList(pageable);
    
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(resultMap));
    }

    @GetMapping("/test/sms")
    public ResponseEntity<?> test() throws Exception {

        smsService.sendToOwner("01055283638", "테스트 문자입니다!!!");
    
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok("OK"));
    }
}
