package it.korea.app_bmpc.filter;

import java.io.IOException;
import java.util.Arrays;

import org.json.JSONObject;
import org.springframework.web.filter.GenericFilterBean;

import it.korea.app_bmpc.common.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtils jwtUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        process((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void process(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
            throws IOException, ServletException {

        // 요청한 경로 가져오기
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        // 경로에 로그아웃이 없거나 HttpMethod 가 POST 가 아니라면 그냥 무시한다
        if (!requestURI.contains("logout") || !requestMethod.equalsIgnoreCase("POST")) {
            chain.doFilter(request, response);
            return;
        }

        String refreshToken = "";
        Cookie[] cookies = request.getCookies();

        response.setContentType("application/json");   // 던지는 데이터 타입이 json 임을 명시해줘야 함

        try {
            
            if (cookies == null) {
                throw new IllegalStateException("쿠키에 정보가 없음");
            }

            refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refresh"))
                .map(Cookie::getValue)
                .findAny()
                .orElseThrow(()-> new IllegalStateException("없음"));
            
            if (jwtUtils.getExpired(refreshToken)) {
                throw new IllegalStateException("refresh 토큰 유효기간 지남");
            }
            
            String category = jwtUtils.getCategory(refreshToken);
            if (!category.equals("refresh")) {
                throw new IllegalStateException("맞지 않는 키");
            }

            // refresh 토큰 쿠키 지우기
            Cookie cookie = new Cookie("refresh", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");

            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("resultMsg", "OK");
            jsonObject.put("status", HttpServletResponse.SC_OK);

            response.getWriter().write(jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("resultMsg", "FAIL");
            jsonObject.put("status", HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().write(jsonObject.toString());
        }
    }
}
