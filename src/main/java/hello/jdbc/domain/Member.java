package hello.jdbc.domain;

import lombok.Data;

@Data
public class Member {
    private String memberId;
    private int money;

    //생성자를 두개 만드는 이유?
    public Member(){
    }

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }
}
