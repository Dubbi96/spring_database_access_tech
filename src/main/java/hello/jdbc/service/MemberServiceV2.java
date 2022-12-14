package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException{
        Connection con = dataSource.getConnection();
        try{
            con.setAutoCommit(false); //트랜잭션 시작 **중요**
            bizLogic(con, fromId, toId, money);
        }catch (Exception e){
            con.rollback(); //실패 시 롤백
            log.error("SQLException",e);
            throw new IllegalStateException(e);
        }finally {
            releaseConnection(con);
        }

    }

    private void releaseConnection(Connection con) {
        if(con != null){
            try{
                con.setAutoCommit(true); //default 값으로 변경 후 Connection Pool로 복귀
                con.close();
            }catch (Exception e){
                log.info("error",e);
            }
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId, con);
        Member toMember = memberRepository.findById(toId, con);

        memberRepository.update(fromId, fromMember.getMoney()- money, con);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney()+ money, con);
        con.commit();
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
