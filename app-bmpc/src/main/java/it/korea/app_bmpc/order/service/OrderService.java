package it.korea.app_bmpc.order.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.korea.app_bmpc.common.dto.PageVO;
import it.korea.app_bmpc.menu.entity.MenuEntity;
import it.korea.app_bmpc.menu.entity.MenuOptionEntity;
import it.korea.app_bmpc.menu.repository.MenuOptionRepository;
import it.korea.app_bmpc.menu.repository.MenuRepository;
import it.korea.app_bmpc.order.dto.OrderDTO;
import it.korea.app_bmpc.order.dto.OrderSearchDTO;
import it.korea.app_bmpc.order.dto.OrderStatusDTO;
import it.korea.app_bmpc.order.entity.OrderEntity;
import it.korea.app_bmpc.order.entity.OrderItemEntity;
import it.korea.app_bmpc.order.entity.OrderItemOptionEntity;
import it.korea.app_bmpc.order.repository.OrderRepository;
import it.korea.app_bmpc.order.repository.OrderSearchSpecification;
import it.korea.app_bmpc.store.entity.StoreEntity;
import it.korea.app_bmpc.user.entity.UserEntity;
import it.korea.app_bmpc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;

    /**
     * 주문 내역 리스트 가져오기
     * @param pageable 페이징 객체
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getOrderList(Pageable pageable) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        Page<OrderEntity> pageList = orderRepository.findAll(pageable);

        List<OrderDTO.Response> orderList = pageList.getContent().stream().map(OrderDTO.Response::of).toList();

        PageVO pageVO = new PageVO();
        pageVO.setData(pageList.getNumber(), (int) pageList.getTotalElements());

        resultMap.put("total", pageList.getTotalElements());
        resultMap.put("content", orderList);
        resultMap.put("pageHTML", pageVO.pageHTML());
        resultMap.put("page", pageList.getNumber());
        
        return resultMap;
    }

    /**
     * 주문 내역 리스트 가져오기 (with Axios 호출 or 검색)
     * @param pageable 페이징 객체
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getOrderList(Pageable pageable, OrderSearchDTO searchDTO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        Page<OrderEntity> pageList = null;

        OrderSearchSpecification searchSpecification = new OrderSearchSpecification(searchDTO);
        pageList = orderRepository.findAll(searchSpecification, pageable);

        List<OrderDTO.Response> orderList = pageList.getContent().stream().map(OrderDTO.Response::of).toList();

        PageVO pageVO = new PageVO();
        pageVO.setData(pageList.getNumber(), (int) pageList.getTotalElements());

        resultMap.put("total", pageList.getTotalElements());
        resultMap.put("content", orderList);
        resultMap.put("pageHTML", pageVO.pageHTML());
        resultMap.put("page", pageList.getNumber());
        
        return resultMap;
    }

    /**
     * 나의 주문 내역 리스트 가져오기
     * @param pageable 페이징 객체
     * @param userId 사용자 아이디
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getMyOrderList(Pageable pageable, String userId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        Page<OrderEntity> pageList = orderRepository.findAllByUser_userId(userId, pageable);

        List<OrderDTO.Response> orderList = pageList.getContent().stream().map(OrderDTO.Response::of).toList();

        PageVO pageVO = new PageVO();
        pageVO.setData(pageList.getNumber(), (int) pageList.getTotalElements());

        resultMap.put("total", pageList.getTotalElements());
        resultMap.put("content", orderList);
        resultMap.put("pageHTML", pageVO.pageHTML());
        resultMap.put("page", pageList.getNumber());
        
        return resultMap;
    }

    /**
     * 나의 주문 내역 리스트 가져오기 (with Axios 호출 or 검색)
     * @param pageable 페이징 객체
     * @param searchDTO 검색 객체
     * @param userId 사용자 아이디
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getMyOrderList(Pageable pageable, OrderSearchDTO searchDTO, String userId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        Page<OrderEntity> pageList = null;

        searchDTO.setUserId(userId);
        OrderSearchSpecification searchSpecification = new OrderSearchSpecification(searchDTO);
        pageList = orderRepository.findAll(searchSpecification, pageable);

        List<OrderDTO.Response> orderList = pageList.getContent().stream().map(OrderDTO.Response::of).toList();

        PageVO pageVO = new PageVO();
        pageVO.setData(pageList.getNumber(), (int) pageList.getTotalElements());

        resultMap.put("total", pageList.getTotalElements());
        resultMap.put("content", orderList);
        resultMap.put("pageHTML", pageVO.pageHTML());
        resultMap.put("page", pageList.getNumber());
        
        return resultMap;
    }

    /**
     * 가게 주문 내역 리스트 가져오기 (with Axios 호출)
     * @param pageable 페이징 객체
     * @param storeId 가게 아이디
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStoreOrderList(Pageable pageable, int storeId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        Page<OrderEntity> pageList = orderRepository.findAllByStore_storeId(storeId, pageable);

        List<OrderDTO.Response> orderList = pageList.getContent().stream().map(OrderDTO.Response::of).toList();

        PageVO pageVO = new PageVO();
        pageVO.setData(pageList.getNumber(), (int) pageList.getTotalElements());

        resultMap.put("total", pageList.getTotalElements());
        resultMap.put("content", orderList);
        resultMap.put("pageHTML", pageVO.pageHTML());
        resultMap.put("page", pageList.getNumber());
        
        return resultMap;
    }

    /**
     * 주문 상세정보 가져오기
     * @param orderId 주문 아이디
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public OrderDTO.Detail getOrder(int orderId) throws Exception {
        return OrderDTO.Detail.of(orderRepository.getOrder(orderId)
            .orElseThrow(()-> new RuntimeException("해당 주문이 존재하지 않습니다.")));
    }

    /**
     * 나의 주문 상세정보 가져오기
     * @param orderId 주문 아이디
     * @param userId 유저 아이디
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public OrderDTO.Detail getOrder(int orderId, String userId) throws Exception {

        OrderDTO.Detail dto = OrderDTO.Detail.of(orderRepository.getOrder(orderId)
            .orElseThrow(()-> new RuntimeException("해당 주문이 존재하지 않습니다.")));
        
        if (!dto.getUserId().equals(userId)) {   // 로그인 된 유저의 아이디와 주문자 아이디가 일치하지 않을 경우...
            throw new RuntimeException("다른 사용자의 주문 상세정보는 볼 수 없습니다.");
        }

        return dto;
    }

    /**
     * 메뉴 주문하기
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional
    public void orderMenu(OrderDTO.Request request) throws Exception {
      
        // 주문할 메뉴가 없으면 예외 발생
        if (request.getMenuList() == null || request.getMenuList().isEmpty()) {
            throw new RuntimeException("주문하려는 메뉴가 없습니다.");
        }

        // 유저가 없으면 예외 발생
        UserEntity userEntity = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        // 주문 생성
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(userEntity);
        orderEntity.setOrderDate(LocalDateTime.now());
        orderEntity.setStatus("주문완료");
        orderEntity.setAddr(request.getAddr());
        orderEntity.setAddrDetail(request.getAddrDetail());

        // 메뉴 아이디 전체 추출
        List<Integer> menuIds = request.getMenuList().stream()
            .map(OrderDTO.InnerRequest::getMenuId)
            .toList();

        // 메뉴 엔티티들 미리 일괄 조회
        List<MenuEntity> menuList = menuRepository.findAllByMenuIdList(menuIds);

        if (menuList.isEmpty()) {
            throw new RuntimeException("해당 메뉴들이 존재하지 않습니다.");
        }

        // Map<menuId, MenuEntity> 로 메뉴 엔티티들 저장해놓기
        Map<Integer, MenuEntity> menuMap = menuList.stream()
            .collect(Collectors.toMap(MenuEntity::getMenuId, m -> m));

        // 옵션 아이디 전체 수집
        List<Integer> optionIdList = request.getMenuList().stream()
            .filter(m -> m.getOptionList() != null)
            .flatMap(m -> m.getOptionList().stream())
            .map(OrderDTO.InnerOptionRequest::getMenuOptId)
            .distinct()
            .toList();

        // 옵션 일괄 조회
        Map<Integer, MenuOptionEntity> optionMap = optionIdList.isEmpty() ? Map.of() :
            menuOptionRepository.findAllById(optionIdList).stream()
                .collect(Collectors.toMap(MenuOptionEntity::getMenuOptId, m -> m));

        // 주문 항목 구성
        int totalOrderPrice = 0;
        int originalDeposit = userEntity.getDeposit();

        for (OrderDTO.InnerRequest innerRequest : request.getMenuList()) {
            MenuEntity menuEntity = menuMap.get(innerRequest.getMenuId());
            if (menuEntity == null) {
                throw new RuntimeException("해당 메뉴가 존재하지 않습니다. menuId=" + innerRequest.getMenuId());
            }

            if ("Y".equals(menuEntity.getSoldoutYn())) {
                throw new RuntimeException("품절된 메뉴입니다. menuId=" + menuEntity.getMenuId());
            }

            int quantity = innerRequest.getQuantity();
            int totalItemPrice = quantity * menuEntity.getPrice();

            // 주문 아이템 생성
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setOrder(orderEntity);
            orderItemEntity.setMenu(menuEntity);
            orderItemEntity.setMenuName(menuEntity.getMenuName());
            orderItemEntity.setMenuPrice(menuEntity.getPrice());
            orderItemEntity.setQuantity(quantity);

            // 옵션 처리
            if (innerRequest.getOptionList() != null) {
                for (OrderDTO.InnerOptionRequest innerOptionRequest : innerRequest.getOptionList()) {
                    MenuOptionEntity menuOptionEntity = optionMap.get(innerOptionRequest.getMenuOptId());
                    if (menuOptionEntity == null) {
                        throw new RuntimeException("해당 옵션이 존재하지 않습니다. menuOptId=" + innerOptionRequest.getMenuOptId());
                    }

                    OrderItemOptionEntity orderItemOptionEntity = new OrderItemOptionEntity();
                    orderItemOptionEntity.setOrderItem(orderItemEntity);
                    orderItemOptionEntity.setMenuOption(menuOptionEntity);
                    orderItemOptionEntity.setMenuOptName(menuOptionEntity.getMenuOptName());
                    orderItemOptionEntity.setMenuOptPrice(menuOptionEntity.getPrice());
                    orderItemOptionEntity.setQuantity(innerOptionRequest.getQuantity());

                    int optionTotal = menuOptionEntity.getPrice() * innerOptionRequest.getQuantity();
                    orderItemOptionEntity.setTotalPrice(optionTotal);
                    totalItemPrice += optionTotal;

                    orderItemEntity.addItemOption(orderItemOptionEntity);
                }
            }

            orderItemEntity.setTotalPrice(totalItemPrice);
            totalOrderPrice += totalItemPrice;

            orderEntity.addItems(orderItemEntity);
        }

        // 한 주문에 오직 한 가게의 메뉴만 포함되어 있는지 검사
        Set<Integer> storeIdList = menuList.stream()
            .map(m -> m.getMenuCategory().getStore().getStoreId())
            .collect(Collectors.toSet());

        if (storeIdList.size() > 1) {
            throw new RuntimeException("한 주문에 여러 가게의 메뉴가 포함되어 있습니다.");
        }

        StoreEntity store = menuList.get(0).getMenuCategory().getStore();
        orderEntity.setStore(store);

        // 총 주문 금액 검증
        if (totalOrderPrice > originalDeposit) {
            throw new RuntimeException("총 주문 금액이 보유금을 초과했습니다.");
        }

        // 보유금 차감
        userEntity.setDeposit(originalDeposit - totalOrderPrice);
        orderEntity.setTotalPrice(totalOrderPrice);

        // 주문 저장
        orderRepository.save(orderEntity);

    }

    /**
     * 주문 상태 변경하기
     * @param orderId 주문 아이디
     * @throws Exception
     */
    @Transactional
    public void updateStatus(OrderStatusDTO statusDTO) throws Exception {

        OrderEntity orderEntity = orderRepository.findById(statusDTO.getOrderId())
            .orElseThrow(()-> new RuntimeException("해당 주문이 존재하지 않습니다."));

        orderEntity.setStatus(statusDTO.getNewStatus());

        if ("주문취소".equals(statusDTO.getNewStatus())) {  // 주문취소일 경우...
            UserEntity userEntity = orderEntity.getUser();

            int originalDeposit = userEntity.getDeposit();
            int totalPrice = orderEntity.getTotalPrice();

            userEntity.setDeposit(originalDeposit + totalPrice);  // 주문이 취소되었기 때문에 보유금 원상복구
        }

        orderRepository.save(orderEntity);
    }
}
