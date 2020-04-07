package persistence.finance.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.finance.record.CreateFinanceRecordDto;
import persistence.EntityId;
import persistence.account.Account;
import persistence.category.Category;
import persistence.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_RECORD_INFO")
public class UserRecord {
    @Id
    @Column(name = "record_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    public void create(CreateFinanceRecordDto data, EntityManager em) {
        Record record = new Record(
                UUID.randomUUID().toString(),
                em.find(Category.class, data.getCategory().getId()),
                em.find(Account.class, data.getAccount().getId()),
                data.getAmount(),
                data.getDescription(),
                data.getRecordDate(),
                new Date(),
                data.changeAccountBalance
        );
        em.persist(record);
        setId(record.getId());
        setRecord(record);
    }
}
