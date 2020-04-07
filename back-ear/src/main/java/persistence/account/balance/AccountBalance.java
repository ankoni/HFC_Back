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
    private BigDecimal balance;

    public AccountBalance(Account account, BigDecimal balance, Date date) {
        AccountBalanceId accountBalanceId = new AccountBalanceId(account, date);
        setBalance(balance);
    }

    public void changeBalance(Category category, BigDecimal amount) {
        if (category.isIncome()) {
            setBalance(getBalance().add(amount));
        } else if (category.isConsumption()) {
            setBalance(getBalance().subtract(amount));
        }
    }

}
