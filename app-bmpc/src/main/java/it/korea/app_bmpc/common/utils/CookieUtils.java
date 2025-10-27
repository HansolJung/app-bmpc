package it.korea.app_bmpc.common.utils;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtils {


    // 토큰 저장
    public Cookie createCookie(String name, Object value, int maxAge) {
        Cookie cookie = new Cookie(name, String.valueOf(value));
        cookie.setPath("/");   // 쿠키의 적용 범위. 전부로 설정
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);   // 자바 스크립트에서 접근 금지

        return cookie;
    }

    // 토큰을 등록
    public void addCookie(Cookie cookie, HttpServletResponse response) {
        response.addCookie(cookie);
    }

    // 쿠키 조회
    public Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        // 쿠키를 찾으면 바로 return 하고 함수 종료
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }

        // 해당 이름을 가진 쿠키가 존재하지 않기 때문에 null return
        return null;
    }

    // 쿠키 삭제
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie cookie = getCookie(request, name);

        if (cookie != null) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);  // 유효 시간 0 = 삭제 처리와 동일
            response.addCookie(cookie);
        }
    }
}

