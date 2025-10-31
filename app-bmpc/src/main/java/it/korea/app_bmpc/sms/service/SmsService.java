package it.korea.app_bmpc.sms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * SMS 발송 서비스
 */
@Slf4j
@Service
public class SmsService {

    private final DefaultMessageService messageService;
    private final String fromNumber;

    public SmsService(@Value("${coolsms.api.key}") String apiKey,
                      @Value("${coolsms.api.secret}") String apiSecret,
                      @Value("${coolsms.api.number}") String fromNumber) {
        this.messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);
        this.fromNumber = fromNumber;
    }

    /**
     * 점주에게 주문 내용 SMS 발송하기
     * @param toNumber 점주 전화번호
     * @param orderSummary 주문 요약
     */
    @Async
    public void sendToOwner(String toNumber, String orderSummary) {
        try {

            Message message = new Message();
            message.setFrom(fromNumber);
            message.setTo(toNumber);
            message.setText("[배달의민족]\n새 주문이 들어왔습니다.\n\n주문내역)\n" + orderSummary);

            this.messageService.send(message);

            log.info("점주 번호 {}로 문자 전송 성공", toNumber);
        } catch (Exception e) {
            log.error("점주 번호 {}로 문자 전송 실패. {}", toNumber, e.getMessage());
        }
    }
}
