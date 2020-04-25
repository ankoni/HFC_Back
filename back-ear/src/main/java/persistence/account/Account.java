package persistence.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.account.balance.AccountBalance;
import persistence.category.Category;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNT")
public class Account implements Serializable {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private Double balance;

    /**
     * Дата изменения счета
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "CHANGE_DATE")
    private Date changeDate;
    /**
     * Дата создания
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "ADD_DATE")
    private Date addDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "DEL_DATE")
    private Date delDate;

    public Account(String id, String name, Double balance, Date changeDate, Date addDate) {
        setId(id);
        setName(name);
        setBalance(balance);
        setChangeDate(changeDate);
        setAddDate(addDate);
    }

    public void changeBalance(Category category, Double amount, boolean deleteRecord) {
        if ((category.isIncome() && !deleteRecord) || (deleteRecord && category.isConsumption())) {
            setBalance(getBalance() + amount);
        } else if ((category.isConsumption() && !deleteRecord) || (deleteRecord && category.isIncome())) {
            setBalance(getBalance() - amount);
        }
        setChangeDate(new Date());
    }
}
