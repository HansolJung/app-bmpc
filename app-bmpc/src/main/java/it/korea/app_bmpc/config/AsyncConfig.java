package it.korea.app_bmpc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * SMS 발송을 비동기 처리 하기위한 설정 파일
 */
@Configuration
@EnableAsync
public class AsyncConfig {

}
