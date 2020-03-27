package persistence.privilege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRIVILEGE")
public class Privilege implements Serializable {
    @Id
    public String id;

    @Column(name = "name")
    public String name;

    @OneToMany(mappedBy = "privilege")
    private List<User> users;
}
