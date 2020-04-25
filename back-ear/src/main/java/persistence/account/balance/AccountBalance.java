package persistence.account.balance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.account.Account;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public AccountBalance getAccountBalance(Account account, Date date, EntityManager entityManager) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date createDate =  format.parse(format.format(date));
        return entityManager.find(AccountBalance.class,
                        new AccountBalanceId(account, createDate));
    }
}
