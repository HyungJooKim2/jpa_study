package study.jpa_study.api;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.jpa_study.domain.Address;
import study.jpa_study.domain.Order;
import study.jpa_study.domain.OrderStatus;
import study.jpa_study.repository.OrderRepository;
import study.jpa_study.repository.OrderSearch;
import study.jpa_study.repository.order.simplequery.OrderSimpleQueryDto;
import study.jpa_study.repository.order.simplequery.OrderSimpleQueryRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 *
 * xToOne(ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 *
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository; //의존관계 주입

    /**
     * V1. 엔티티 직접 노출 -> 안좋은 사례
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     * - 지연 로딩(LAZY)을 피하기 위해 즉시 로딩(EARGR)으로 설정하면 안된다. -> fetch join 을 사용 권장
     * - 즉시 로딩 때문에 연관관계가 필요 없는 경우에도 데이터를 항상 조회시 성능 튜닝이 매우 어려워 진다.
     * -
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
        return all;
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출           n번                n번
     * 지금에서 쿼리가 1 + N + N 번 실행된다. order -> member , order -> delivery
     * 예) order 의 결과가 2개인 경우 1 + 2 + 2 = 5번 실행된다.
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll();

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());

        return result;
    }

    /**
     * V3. 엔티티를 페치 조인(fetch join)을 사용해서 쿼리 1번에 조회
     * - 페치 조인으로 order -> member , order -> delivery 는 이미 조회 된 상태 이므로 지연로딩X
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    /**
     * V4. JPA 에서 DTO 로 바로 조회
     * - 쿼리 1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회 (join 은 동일 select 에서 불필요한 것을 x)
     * - 재사용성에서는 V3 에 비해서 떨어질 수 있다. (일일이 지정을 해놓았으니)
     * - Entity 가 아닌 dto 로 조회하였기 떄문에 값을 아예 건드릴 수 없으며 코드가 V3 에 비해 더 길다.
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }


    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
