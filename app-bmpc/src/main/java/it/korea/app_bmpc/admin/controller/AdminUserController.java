package it.korea.app_bmpc.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.korea.app_bmpc.admin.dto.AdminUserDTO;
import it.korea.app_bmpc.admin.service.AdminUserService;
import it.korea.app_bmpc.order.dto.OrderDTO;
import it.korea.app_bmpc.order.service.OrderService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    private final AdminUserService userService;
    private final OrderService orderService;

    /**
     * 회원 리스트
     * @param pageable 페이징 객체
     * @return
     */
    @GetMapping("/list")
    public ModelAndView getUserListView(@PageableDefault(page = 0, size = 10, 
            sort = "createDate", direction = Direction.DESC) Pageable pageable) throws Exception {

        ModelAndView view = new ModelAndView();
        Map<String, Object> resultMap = new HashMap<>();

        resultMap = userService.getUserList(pageable);

        view.addObject("data", resultMap);
        view.setViewName("views/admin/list");
        
        return view;
    }

    /**
     * 회원 등록 폼
     * @return
     */
    @GetMapping("/create/form")
    public ModelAndView getCreateForm() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/admin/createForm");
        
        return view;
    }

    /**
     * 회원 수정 폼
     * @param userId 회원 아이디
     * @return
     * @throws Exception
     */
    @GetMapping("/info/{userId}")
    public ModelAndView getUpdateForm(@PathVariable(name = "userId") String userId) throws Exception {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/admin/detail");

        AdminUserDTO userDTO = userService.getUser(userId);
        view.addObject("vo", userDTO);
        
        return view;
    }

    /**
     * 주문 내역 리스트
     * @param pageable 페이징 객체
     * @return
     */
    @GetMapping("/order/list")
    public ModelAndView getOrderListView(@PageableDefault(page = 0, size = 10, 
            sort = "orderDate", direction = Direction.DESC) Pageable pageable) throws Exception {

        ModelAndView view = new ModelAndView();
        Map<String, Object> resultMap = new HashMap<>();

        resultMap = orderService.getOrderList(pageable);

        view.addObject("data", resultMap);
        view.setViewName("views/admin/orderList");
        
        return view;
    }

    /**
     * 주문 상세 뷰
     * @param orderId 주문 아이디
     * @return
     */
    @GetMapping("/order/detail/{orderId}")
    public ModelAndView getOrderDetailView(@PathVariable(name = "orderId") int orderId) throws Exception {

        ModelAndView view = new ModelAndView();
        OrderDTO.Detail dto = orderService.getOrder(orderId);

        view.addObject("vo", dto);
        view.setViewName("views/admin/orderDetail");
        
        return view;
    }

}
