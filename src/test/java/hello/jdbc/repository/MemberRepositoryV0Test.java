package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        //Create
        Member member = new Member("memberV7", 10000);
        memberRepositoryV0.save(member);

        //Read
        Member findMember = memberRepositoryV0.findById(member.getMemberId());
        log.info("findMember = {}", findMember);
        assertThat(findMember).isEqualTo(member);

        //Update : money : 10000 -> 20000
        memberRepositoryV0.update(member.getMemberId(),20000);
        Member updatedMember = memberRepositoryV0.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        //Delete
        memberRepositoryV0.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> memberRepositoryV0.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}