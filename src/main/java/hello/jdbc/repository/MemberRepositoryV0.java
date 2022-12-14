package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException{
        String sql = "insert into member(member_id, money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,member.getMemberId());
            pstmt.setInt(2,member.getMoney());
            int affectedRows = pstmt.executeUpdate();
            log.info("{} rows affected ",affectedRows);
            return member;
        }catch(SQLException e){
            log.error("DB ERROR!", e);
            throw e;
        }finally{
            close(con,pstmt,null);
            /**pstmt.close(); //여기서 exception 터지면 아래 connection이 안 끊어짐
             * con.close();* */
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBConnectionUtil.getConnection(); //get connection 만들어 놓은 함수로 시작하게 설정 꼭 해주십쇼.
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);

            rs = pstmt.executeQuery();
            if (rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not fond memberId=" + memberId);
            }
        } catch (SQLException e) {
            log.info("db error ",e);
            throw e;
        } finally {
            close(con,pstmt,rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {

        String sql = "update member set money = ? where member_id =?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,money);
            pstmt.setString(2,memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("{} rows affected ",resultSize);
        }catch(SQLException e){
            log.error("DB ERROR!", e);
            throw e;
        }finally{
            close(con,pstmt,null);
            /**pstmt.close(); //여기서 exception 터지면 아래 connection이 안 끊어짐
             * con.close();* */
        }
    }
    public void delete(String memberId) throws SQLException {

        String sql = "delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("{} rows affected ", resultSize);
        } catch (SQLException e) {
            log.error("DB ERROR!", e);
            throw e;
        } finally {
            close(con, pstmt, null);
            /**pstmt.close(); //여기서 exception 터지면 아래 connection이 안 끊어짐
             * con.close();* */
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs){
        if(rs != null){
            try{
                rs.close();
            }catch (SQLException e){
                log.info("error", e);
            }
        }
        if(stmt != null){
            try{
                stmt.close();
            }catch (SQLException e){
                log.info("error", e);
            }
        }
        if(con != null){
            try{
                con.close();
            }catch (SQLException e){
                log.info("error", e);
            }
        }
    }
}
