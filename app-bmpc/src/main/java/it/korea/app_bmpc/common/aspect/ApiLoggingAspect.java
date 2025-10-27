package it.korea.app_bmpc.common.aspect;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import it.korea.app_bmpc.common.utils.TimeFormatUtils;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class ApiLoggingAspect {

    /**
     * 모든 컨트롤러에 적용
     * @param joinPoint
     */
    @Before("execution(* it.korea.app_bmpc.*.controller..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        // 현재 요청(Request) 가져오기
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();

            // 요청 기본 정보
            String uri = request.getRequestURI();
            String httpMethod = request.getMethod();

            // 메서드 및 클래스 정보
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            // 로그 출력
            log.info("[API 호출됨] {} {} {} {}.{}()",
                TimeFormatUtils.getNowTime(),
                httpMethod,
                uri,
                className,
                methodName
            );
        }
    }
}
