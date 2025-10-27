package it.korea.app_bmpc.filter;

import java.io.IOException;
import java.util.Iterator;

import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import it.korea.app_bmpc.common.utils.JWTUtils;
import it.korea.app_bmpc.user.dto.UserSecureDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 인증 필터를 만든다
 */
@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 86400L;  // 24시간
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 86400L;   // 24시간

    // 인증시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        
        UsernamePasswordAuthenticationToken authRequest = 
                UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        return authenticationManager.authenticate(authRequest);
    }

    // 성공처리
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        UserSecureDTO user = (UserSecureDTO) authResult.getPrincipal();
        String userId = user.getUserId();
        String userName = user.getUserName();

        // 권한
        // 와일드카드 사용. 무엇이 올지는 모르지만 GrantedAuthority 를 상속받은 것들이 들어온다.
        Iterator<? extends GrantedAuthority> iter = authResult.getAuthorities().iterator(); 
        String userRole = iter.next().getAuthority();   // 권한을 한 개만 가지는 상황이기 때문에 바로 꺼내기

        // 토큰 생성
        String accessToken = jwtUtils.createJwt("access", userId, userName, userRole, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtUtils.createJwt("refresh", userId, userName, userRole, REFRESH_TOKEN_EXPIRE_TIME);

        // 응답을 설정
        response.setHeader("Authorization", accessToken);  // 일반 토큰은 헤더에 저장. 서로 왔다갔다 하면서 인증을 해야하기 때문.
        response.addCookie(createCookie("refresh", refreshToken));   // 인증이 실패했을 경우에 사용하라고 response 에 refreshToken 을 넣는것
        response.setStatus(HttpServletResponse.SC_OK);

        // JSON 전송
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("resultMsg", "OK");
            jsonObject.put("status", HttpServletResponse.SC_OK);

            JSONObject data = new JSONObject();
            data.put("userId", userId);
            data.put("userName", userName);
            data.put("userRole", userRole);
            data.put("token", accessToken);

            jsonObject.put("content", data);

            response.setContentType("application/json");   // 던지는 데이터 타입이 json 임을 명시해줘야 함
            //response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 실패처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        
        response.setContentType("application/json");   // 던지는 데이터 타입이 json 임을 명시해줘야 함
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // JSON 전송
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("resultMsg", "FAIL");
            jsonObject.put("status", HttpServletResponse.SC_UNAUTHORIZED);

            JSONObject data = new JSONObject();
            jsonObject.put("content", data);

            response.setContentType("application/json");   // 던지는 데이터 타입이 json 임을 명시해줘야 함
            //response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 토큰 저장
    private Cookie createCookie(String name, Object value) {
        Cookie cookie = new Cookie(name, String.valueOf(value));
        cookie.setPath("/");   // 쿠키의 적용 범위. 전부로 설정
        cookie.setMaxAge((int) REFRESH_TOKEN_EXPIRE_TIME);
        cookie.setHttpOnly(true);   // 자바스크립트에서 접근 금지

        return cookie;
    }
}
