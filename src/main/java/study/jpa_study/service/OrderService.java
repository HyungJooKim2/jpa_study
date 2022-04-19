package study.jpa_study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jpa_study.domain.*;
import study.jpa_study.domain.item.Item;
import study.jpa_study.repository.ItemRepository;
import study.jpa_study.repository.MemberRepository;
import study.jpa_study.repository.OrderRepository;
import study.jpa_study.repository.OrderSearch;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     * @param memberId 주문자 식별자
     * @param itemId 아이템 식별자
     * @param count 아이템 수량
     * @return 해당 주문 식별자 반환
     * */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성 (우선 회원의 주소로 전송)
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        /*
        주문 저장 cascade 가 되어있어 order 를 persist 하면 orderItems, delivery 가 save 된다.
        cascade 범위를 너무 넓게 가져가선 안된다. (Order 가 지워질 경우 아이템, 배송 정보가 지워짐)
        Delivery, OrderItems 를 다른곳에서 참조하지 않는 경우 정도에만
         */
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     * @param orderId 주문 식별자
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
