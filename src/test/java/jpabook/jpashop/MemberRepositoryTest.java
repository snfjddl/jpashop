package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false) // 롤백 안하고 그냥 저장
    public void testMember() throws Exception {
        Member member = new Member();
        member.setUsername("BB");

        Long saveId = memberRepository.save(member);

        Member findMember = memberRepository.find(saveId);

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());

//        영속성 컨택스트에 저장되어 관리되기 때문에 1차 캐시에 있는 data를 바로 가져옴. 따라서 select문 조차도 실행하지 않음
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}