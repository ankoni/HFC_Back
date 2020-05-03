package persistence.privilege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.auth.Permission;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ACCESS_TABLE")
public class Access {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private Permission name;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_date")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "privilege_id")
    private Privilege privilege;
}
