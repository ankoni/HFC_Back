package persistence.user;
import lombok.*;
import org.hibernate.metamodel.source.annotations.entity.EntityClass;
import persistence.account.UserAccount;
import persistence.privilege.Privilege;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_DATA")
public class User implements Serializable {
    @Id
    public String id;

    @Column(name = "name")
    public String name;

    @Column(name = "password")
    public String password;

    @Temporal(TemporalType.DATE)
    @Column(name = "reg_date")
    public Date regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "privilege_ID")
    public Privilege privilege;

    public User(String id, String name, String password, Privilege privilege) {
        this.setId(id);
        this.setName(name);
        this.setPassword(password);
        this.setRegDate( new Date() );
        this.setPrivilege(privilege);
    }
}
