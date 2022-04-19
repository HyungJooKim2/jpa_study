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

    @OneToMany(mappedBy = "member") //order에 있는 member 필드에 의해 매핑이 되어 있다.
    private List<Order> orders = new ArrayList<>();
}
