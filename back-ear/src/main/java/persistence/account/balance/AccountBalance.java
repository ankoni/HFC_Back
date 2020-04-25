package persistence.account.balance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.account.Account;
import persistence.category.Category;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNT_BALANCE")
public class AccountBalance {
    @Id
    private AccountBalanceId accountBalanceId;

    @Column(name = "balance")
    private Double balance;

    public AccountBalance(Account account, Date date, Double balance) {
        AccountBalanceId accountBalanceId = new AccountBalanceId(account, date);
        setBalance(balance);
    }

    public void changeBalance(Category category, Double amount) {
        if (category.isIncome()) {
            setBalance(getBalance() + amount);
        } else if (category.isConsumption()) {
            setBalance(getBalance() - amount);
        }
    }

}
