package study.jpa_study;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import study.jpa_study.domain.Member;

@RunWith(SpringRunner.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false) //Test가 끝나면 roll back 방지
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setName("memberA");

        //when entity manager를 통한 모든 데이터 변경은 트렌젝션 안에서 이루어져야 한다.
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertEquals(findMember.getId(),member.getId());
        Assertions.assertEquals(findMember.getName(),member.getName());
        Assertions.assertEquals(findMember,member);
    }

}