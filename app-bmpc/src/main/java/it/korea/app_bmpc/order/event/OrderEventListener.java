package it.korea.app_bmpc.order.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import it.korea.app_bmpc.order.entity.OrderEntity;
import it.korea.app_bmpc.order.entity.OrderItemEntity;
import it.korea.app_bmpc.order.repository.OrderRepository;
import it.korea.app_bmpc.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final SmsService smsService;
    private final OrderRepository orderRepository;

    /**
     * 트랜잭션이 끝난 후 SMS 발송 이벤트 수행
     * @param event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        try {
            
            OrderEntity orderEntity = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("해당 주문 정보를 찾을 수 없습니다."));

            String orderSummary = buildOrderSummary(orderEntity);

            smsService.sendToOwner(event.getOwnerPhone(), orderSummary);

            log.info("주문 {}번에 대한 문자 발송 완료", event.getOrderId());
        } catch (Exception e) {
            log.error("주문 {}번에 대한 문자 발송 중 오류 발생. {}", event.getOrderId(), e.getMessage());
        }
    }

    /**
     * 주문내역 요약본 만들기
     * @param orderEntity
     * @return
     */
    private String buildOrderSummary(OrderEntity orderEntity) {
        StringBuilder sb = new StringBuilder();

        for (OrderItemEntity orderItemEntity : orderEntity.getItemList()) {
            sb.append(" - ")
                .append(orderItemEntity.getMenuName())
                .append(" x").append(orderItemEntity.getQuantity())
                .append("\n");
        }

        return sb.toString();
    }
}
