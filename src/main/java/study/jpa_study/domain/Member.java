package study.jpa_study.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded //내장타입을 매포
    private Address address;

    /**
     * 컬렉션은 필드에서 바로 초기화 하는 것이 안전하다.
     * null 문제에서 안전하다.
     * 하이버네이트는 엔티티를 영속화 할 때, 컬랙션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다.
     * 만약 getOrders() 처럼 임의의 메서드에서 컬력션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문 제가 발생할 수 있다.
     * 따라서 필드레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다.
     */
    @OneToMany(mappedBy = "member") //order에 있는 member 필드에 의해 매핑이 되어 있다.
    private List<Order> orders = new ArrayList<>();
}
