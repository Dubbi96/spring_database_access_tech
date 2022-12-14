package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 memberRepositoryV1;

    @BeforeEach
    void beforeEach() throws InterruptedException {
        //기본 DriverManager - 항상 새로운 커넥션을 획득
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);

        //Connection Pooling
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        memberRepositoryV1 = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException, InterruptedException {
        //Create
        Member member = new Member("memberV1", 10000);
        memberRepositoryV1.save(member);

        //Read
        Member findMember = memberRepositoryV1.findById(member.getMemberId());
        log.info("findMember = {}", findMember);
        assertThat(findMember).isNotNull();

        //Update : money : 10000 -> 20000
        memberRepositoryV1.update(member.getMemberId(),20000);
        Member updatedMember = memberRepositoryV1.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        //Delete
        memberRepositoryV1.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> memberRepositoryV1.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        Thread.sleep(5000);
    }
}