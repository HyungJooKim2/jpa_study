package study.jpa_study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jpa_study.domain.Member;
import study.jpa_study.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true) //읽기용 모드에 추가할 시 성능 향상 데이터 변경할땐 x
@RequiredArgsConstructor        //final 있는 필드만 가지고 생성자를 만들어준다.
public class MemberService {

    //final -> compile 시점을 체크 가능
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional //쓰기 이기 떄문에 따로 설정 readOnly = false
    public Long join(Member member) {

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * 회원 수정
     * 변경감지 적용
     */
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }

}
