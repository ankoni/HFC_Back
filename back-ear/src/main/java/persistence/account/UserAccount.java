package persistence.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.EntityId;
import persistence.user.User;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "USER_ACCOUNT")
public class UserAccount extends EntityId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Account account;

    public UserAccount() {}

    public UserAccount(String id, User user, Account account) {
        super(id);
        setUser(user);
        setAccount(account);
    }
}
