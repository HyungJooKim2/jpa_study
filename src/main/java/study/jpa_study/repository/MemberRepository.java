package study.jpa_study.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.jpa_study.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    //persist : 영속성 컨텍스트에 member Entity 를 넣어 db에 반영이 되게 설정
    public void save(Member member) {
        em.persist(member);
    }
    //단건 조회 : (type, pk)로 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    /*
    sql -> 테이블 대상으로 쿼리 (from 의 대상이 테이블)
    jpql -> 엔티티 객체(Member)를 대상으로 쿼리 (from 의 대상이 엔티티)
    */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}