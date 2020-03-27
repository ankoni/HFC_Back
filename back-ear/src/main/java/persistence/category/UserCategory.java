package persistence.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.EntityId;
import persistence.user.User;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_CATEGORY")
public class UserCategory extends EntityId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Category category;

    public UserCategory(String id, User user, Category category) {
        super(id);
        setUser(user);
        setCategory(category);
    }
}
