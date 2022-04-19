package study.jpa_study.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

/**
 * 값 타입은 변경 불가능하게 설계정
 * 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스로 만들고, JPA 스펙상 엔티티나
 * 임베디드 타입(@Embeddable)은 자바 기본 생성자(default constructor)를 public 또는
 * protected 로 설정해야 한다. public 으로 두는 것 보다는 protected 로 설정하는 것이 안전하다.
 * 이유 : JPA 구현 라이브러리가 객체를 생성할 때 리플랙션 같은 기술을 사용할 수 있도록 지원해야 하기 떄문
 */
@Embeddable //Jpa의 내장 타입
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address(){

    }

    //생성할때만 값이 세팅, 변경 불가하게
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
