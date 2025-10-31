package it.korea.app_bmpc.basket.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.korea.app_bmpc.basket.dto.BasketDTO;
import it.korea.app_bmpc.basket.entity.BasketEntity;
import it.korea.app_bmpc.basket.entity.BasketItemEntity;
import it.korea.app_bmpc.basket.entity.BasketItemOptionEntity;
import it.korea.app_bmpc.basket.repository.BasketRepository;
import it.korea.app_bmpc.menu.entity.MenuEntity;
import it.korea.app_bmpc.menu.entity.MenuOptionEntity;
import it.korea.app_bmpc.menu.repository.MenuOptionRepository;
import it.korea.app_bmpc.menu.repository.MenuRepository;
import it.korea.app_bmpc.order.entity.OrderEntity;
import it.korea.app_bmpc.order.entity.OrderItemEntity;
import it.korea.app_bmpc.order.entity.OrderItemOptionEntity;
import it.korea.app_bmpc.order.event.OrderCreatedEvent;
import it.korea.app_bmpc.order.repository.OrderRepository;
import it.korea.app_bmpc.store.entity.StoreEntity;
import it.korea.app_bmpc.user.entity.UserEntity;
import it.korea.app_bmpc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 장바구니 가져오기
     * @param userId 사용자 아이디
     * @return
     */
    @Transactional(readOnly = true)
    public BasketDTO.Detail getBasket(String userId) {

        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        // 장바구니 조회 (없으면 새로 생성)
        BasketEntity basketEntity = basketRepository.findByUser_userId(userId)
            .orElseGet(() -> {
                BasketEntity newBasketEntity = new BasketEntity();
                newBasketEntity.setUser(userEntity);
                newBasketEntity.setTotalPrice(0);

                // store는 null 상태로 둠
                return basketRepository.save(newBasketEntity);
            });

        return BasketDTO.Detail.of(basketEntity);
    }

    /**
     * 장바구니 전부 주문하기
     * @param request 장바구니 객체
     * @throws Exception
     */
    @Transactional
    public void orderAllMenu(BasketDTO.OrderRequest request) throws Exception {

        BasketEntity basketEntity = basketRepository.findByUser_userId(request.getUserId())
            .orElseThrow(() -> new RuntimeException("해당 사용자가 가진 장바구니가 존재하지 않습니다."));

        if (basketEntity.getItemList().isEmpty()) {
            throw new RuntimeException("장바구니가 비어 있습니다.");
        }

        // 장바구니 총액
        int totalPrice = basketEntity.getTotalPrice();

        // 사용자 정보 가져오기
        UserEntity userEntity = basketEntity.getUser();

        // 보유금 잔액 확인
        if (userEntity.getDeposit() < totalPrice) {
            throw new RuntimeException("보유금이 부족합니다. 현재 잔액: " 
                + userEntity.getDeposit() + "원, 주문 금액: " + totalPrice + "원");
        }

        // 주문 생성
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(basketEntity.getUser());
        orderEntity.setStore(basketEntity.getStore());
        orderEntity.setOrderDate(LocalDateTime.now());
        orderEntity.setStatus("주문완료");
        orderEntity.setAddr(request.getAddr());
        orderEntity.setAddrDetail(request.getAddrDetail());

        // 장바구니 항목을 주문 아이템으로 변환
        for (BasketItemEntity basketItemEntity : basketEntity.getItemList()) {

            MenuEntity menuEntity = basketItemEntity.getMenu();

            // 메뉴의 품절 여부가 Y 인경우...
            if ("Y".equals(menuEntity.getSoldoutYn())) {
                throw new RuntimeException("[" + menuEntity.getMenuName() + "] 메뉴는 현재 품절 상태입니다. 주문할 수 없습니다.");
            }

            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setMenu(menuEntity);
            orderItemEntity.setMenuName(basketItemEntity.getMenuName());
            orderItemEntity.setMenuPrice(basketItemEntity.getMenuPrice());
            orderItemEntity.setQuantity(basketItemEntity.getQuantity());
            orderItemEntity.setTotalPrice(basketItemEntity.getTotalPrice());

            // 장바구니 옵션을 주문 옵션으로 변환
            for (BasketItemOptionEntity basketItemOptionEntity : basketItemEntity.getItemOptionList()) {
                OrderItemOptionEntity orderItemOptionEntity = new OrderItemOptionEntity();
                orderItemOptionEntity.setMenuOption(basketItemOptionEntity.getMenuOption());
                orderItemOptionEntity.setMenuOptName(basketItemOptionEntity.getMenuOptName());
                orderItemOptionEntity.setMenuOptPrice(basketItemOptionEntity.getMenuOptPrice());
                orderItemOptionEntity.setQuantity(basketItemOptionEntity.getQuantity());
                orderItemOptionEntity.setTotalPrice(basketItemOptionEntity.getTotalPrice());

                // 양방향 연관관계 세팅
                orderItemEntity.addItemOption(orderItemOptionEntity);
            }

            // 주문에 주문 항목 추가
            orderEntity.addItems(orderItemEntity);
        }

       // 주문 총액 저장
        orderEntity.setTotalPrice(totalPrice);

        // 보유금 차감
        int newDeposit = userEntity.getDeposit() - totalPrice;
        userEntity.setDeposit(newDeposit);

        // 주문 저장
        orderRepository.save(orderEntity);

        // 보유금 수정
        userRepository.save(userEntity);

        // 장바구니 비우기
        basketEntity.getItemList().clear();
        basketEntity.setTotalPrice(0);
        basketRepository.save(basketEntity);

        // 점주 전화번호 찾기
        // UserEntity owner = userRepository.findByStore(orderEntity.getStore())
        //     .orElseThrow(() -> new RuntimeException("해당 가게의 점주를 찾을 수 없습니다."));

        // String ownerPhone = owner.getPhone().replace("-", "");

        // 점주에게 sms 발송하기위해 이벤트 발행 (모든 save가 정상적으로 실행이 된 이후 발송됨)
        //eventPublisher.publishEvent(new OrderCreatedEvent(orderEntity.getOrderId(), ownerPhone));
    }

    /**
     * 장바구니에 메뉴 추가하기
     * @param request 장바구니 객체
     * @return
     * @throws Exception
     */
    @Transactional
    public void addMenu(BasketDTO.Request request) throws Exception {

        UserEntity user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        BasketDTO.InnerRequest menuReq = request.getMenu();
        
        if (menuReq == null) {
            throw new RuntimeException("장바구니에 담을 메뉴 요청이 없습니다.");
        }

        MenuEntity menu = menuRepository.findById(menuReq.getMenuId())
            .orElseThrow(() -> new RuntimeException("장바구니에 담을 메뉴가 존재하지 않습니다."));

        StoreEntity store = menu.getMenuCategory().getStore();

        BasketEntity basketEntity = basketRepository.findByUser_userId(request.getUserId())
            .orElse(null);

        if (basketEntity == null) {
            // 장바구니가 없으면 새로 생성
            basketEntity = new BasketEntity();
            basketEntity.setUser(user);
        } else if (basketEntity.getStore() != null && basketEntity.getStore().getStoreId() != store.getStoreId()) {
            // 다른 가게의 메뉴 담으려는 경우는 장바구니 비우기
            basketEntity.getItemList().clear();
        }

        basketEntity.setStore(store);

        // 장바구니 항목 생성
        BasketItemEntity basketItemEntity = new BasketItemEntity();
        basketItemEntity.setMenu(menu);
        basketItemEntity.setMenuName(menu.getMenuName());
        basketItemEntity.setMenuPrice(menu.getPrice());
        basketItemEntity.setQuantity(menuReq.getQuantity());

        // 옵션은 제외한 기본 메뉴 금액
        int totalItemPrice = menu.getPrice() * menuReq.getQuantity();

        // 옵션이 있는 경우는 옵션 id들을 모아서 일괄 조회
        List<BasketDTO.InnerOptionRequest> optionReqList = menuReq.getOptionList();
        Map<Integer, MenuOptionEntity> menuOptionMap = Collections.emptyMap();
        if (optionReqList != null && !optionReqList.isEmpty()) {
            List<Integer> optIdList = optionReqList.stream()
                .map(BasketDTO.InnerOptionRequest::getMenuOptId)
                .distinct()
                .toList();

            List<MenuOptionEntity> menuOptionEntityList = menuOptionRepository.findAllById(optIdList);
            menuOptionMap = menuOptionEntityList.stream()
                .collect(Collectors.toMap(MenuOptionEntity::getMenuOptId, o -> o));
        }

        // 옵션 엔티티 생성 및 item에 추가
        if (optionReqList != null && !optionReqList.isEmpty()) {
            for (BasketDTO.InnerOptionRequest optionReq : optionReqList) {
                MenuOptionEntity menuOption = menuOptionMap.get(optionReq.getMenuOptId());
                if (menuOption == null) {
                    throw new RuntimeException("옵션이 존재하지 않습니다. menuOptId=" + optionReq.getMenuOptId());
                }

                BasketItemOptionEntity basketItemOptionEntity = new BasketItemOptionEntity();
                basketItemOptionEntity.setMenuOption(menuOption);
                basketItemOptionEntity.setMenuOptName(menuOption.getMenuOptName());
                basketItemOptionEntity.setMenuOptPrice(menuOption.getPrice());
                basketItemOptionEntity.setQuantity(optionReq.getQuantity());

                int totalOptionPrice = menuOption.getPrice() * optionReq.getQuantity();
                basketItemOptionEntity.setTotalPrice(totalOptionPrice);

                // 연관관계 설정
                basketItemEntity.addItemOption(basketItemOptionEntity);

                // 옵션 금액을 항목 총액에 추가
                totalItemPrice += totalOptionPrice;
            }
        }

        // 항목 총액 설정 후 장바구니에 추가
        basketItemEntity.setTotalPrice(totalItemPrice);
        basketEntity.addItem(basketItemEntity);

        // 장바구니 총액 계산
        int totalBasketPrice = basketEntity.getItemList().stream().mapToInt(BasketItemEntity::getTotalPrice).sum();
        basketEntity.setTotalPrice(totalBasketPrice);

        // 장바구니 저장
        basketRepository.save(basketEntity);
    }

    /**
     * 장바구니에서 메뉴 삭제하기
     * @param userId 사용자 아이디
     * @param basketItemId 장바구니 항목 아이디
     * @throws Exception
     */
    @Transactional
    public void deleteMenu(String userId, int basketItemId) throws Exception {

        // 장바구니 조회
        BasketEntity basketEntity = basketRepository.findByUser_userId(userId)
            .orElseThrow(() -> new RuntimeException("해당 사용자가 가진 장바구니가 존재하지 않습니다."));

        // 삭제할 장바구니 항목 조회
        BasketItemEntity basketItemEntity = basketEntity.getItemList().stream()
            .filter(item -> item.getBasketItemId() == basketItemId)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("삭제할 메뉴가 장바구니에 없습니다."));

        // 장바구니 항목 삭제
        basketEntity.getItemList().remove(basketItemEntity);

        // 장바구니 총액 계산
        int totalBasketPrice = basketEntity.getItemList().stream()
            .mapToInt(BasketItemEntity::getTotalPrice)
            .sum();

        // 장바구니 총액 변경
        basketEntity.setTotalPrice(totalBasketPrice);

        // 장바구니 저장
        basketRepository.save(basketEntity);
    }

    /**
     * 장바구니 전부 비우기
     * @param userId 사용자 아이디
     * @throws Exception
     */
    @Transactional
    public void deleteAllMenu(String userId) throws Exception {

        // 장바구니 조회
        BasketEntity basket = basketRepository.findByUser_userId(userId)
            .orElseThrow(() -> new RuntimeException("해당 사용자가 가진 장바구니가 존재하지 않습니다."));

        // 장바구니 항목 전부 제거
        basket.getItemList().clear();

        // 장바구니 총액 초기화
        basket.setTotalPrice(0);

        // 장바구니 저장
        basketRepository.save(basket);
    }

    /**
     * 장바구니 항목 수량 증가시키기 (+ 버튼 클릭시)
     * @param userId 사용자 아이디
     * @param basketItemId 장바구니 항목 아이디
     * @throws Exception
     */
    @Transactional
    public void increaseMenuQuantity(String userId, int basketItemId) throws Exception {

        BasketEntity basketEntity = basketRepository.findByUser_userId(userId)
            .orElseThrow(() -> new RuntimeException("해당 사용자가 가진 장바구니가 존재하지 않습니다."));

        BasketItemEntity basketItemEntity = basketEntity.getItemList().stream()
            .filter(item -> item.getBasketItemId() == basketItemId)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("해당 메뉴가 장바구니에 없습니다."));

        basketItemEntity.setQuantity(basketItemEntity.getQuantity() + 1);

        int totalItemPrice = basketItemEntity.getMenuPrice() * basketItemEntity.getQuantity();
        int totalOptionPrice = 0;

        for (BasketItemOptionEntity option : basketItemEntity.getItemOptionList()) {
            option.setQuantity(option.getQuantity() + 1);
            option.setTotalPrice(option.getMenuOptPrice() * option.getQuantity());
            totalOptionPrice += option.getTotalPrice();
        }

        totalItemPrice += totalOptionPrice;
        basketItemEntity.setTotalPrice(totalItemPrice);

        // 장바구니 총액 재계산
        int totalBasketPrice = basketEntity.getItemList().stream()
            .mapToInt(BasketItemEntity::getTotalPrice)
            .sum();
            
        basketEntity.setTotalPrice(totalBasketPrice);

        basketRepository.save(basketEntity);
    }

    /**
     * 장바구니 항목 수량 감소시키기 (- 버튼 클릭시)
     * @param userId 사용자 아이디
     * @param basketItemId 장바구니 항목 아이디
     * @throws Exception
     */
    @Transactional
    public void decreaseMenuQuantity(String userId, int basketItemId) throws Exception {

        BasketEntity basketEntity = basketRepository.findByUser_userId(userId)
            .orElseThrow(() -> new RuntimeException("해당 사용자가 가진 장바구니가 존재하지 않습니다."));

        BasketItemEntity basketItemEntity = basketEntity.getItemList().stream()
            .filter(item -> item.getBasketItemId() == basketItemId)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("해당 메뉴가 장바구니에 없습니다."));

        // 수량이 1 보다 큰 경우에만 감소
        if (basketItemEntity.getQuantity() > 1) {
            basketItemEntity.setQuantity(basketItemEntity.getQuantity() - 1);

            int totalItemPrice = basketItemEntity.getMenuPrice() * basketItemEntity.getQuantity();
            int totalOptionPrice = 0;

            for (BasketItemOptionEntity option : basketItemEntity.getItemOptionList()) {
                option.setQuantity(option.getQuantity() - 1);
                option.setTotalPrice(option.getMenuOptPrice() * option.getQuantity());
                totalOptionPrice += option.getTotalPrice();
            }

            basketItemEntity.setTotalPrice(totalItemPrice + totalOptionPrice);

            // 장바구니 총액 재계산
            int totalBasketPrice = basketEntity.getItemList().stream()
                .mapToInt(BasketItemEntity::getTotalPrice)
                .sum();

            basketEntity.setTotalPrice(totalBasketPrice);

            basketRepository.save(basketEntity);
        }
    }
}
