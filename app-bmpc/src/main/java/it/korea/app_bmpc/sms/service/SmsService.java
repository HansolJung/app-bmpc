package it.korea.app_bmpc.sms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;

@Service
public class SmsService {

    private final DefaultMessageService messageService;
    private final String number;

    public SmsService(@Value("${coolsms.api.key}") String apiKey,
                      @Value("${coolsms.api.secret}") String apiSecret,
                      @Value("${coolsms.api.number}") String number) {
        this.messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);
        this.number = number;
    }

    public DefaultMessageService getMessageService() {
        return messageService;
    }

    public MultipleDetailMessageSentResponse sendOne() {
        try {
            Message message = new Message();
            // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
            message.setFrom(number);
            message.setTo(number);
            message.setText("테스트 문자");

            MultipleDetailMessageSentResponse response = this.messageService.send(message);
            System.out.println(response);

            return response;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return null;
    }
}
