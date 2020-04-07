package impl.account;

import api.account.AccountReadService;
import impl.UserReadServiceImpl;
import model.account.UserAccountData;
import persistence.account.Account;
import persistence.account.UserAccount;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Remote(AccountReadService.class)
@LocalBean
@Stateless(name = "AccountReadService")
public class AccountReadServiceImpl implements AccountReadService {
    @PersistenceContext
    EntityManager entityManager;

    @Inject
    UserReadServiceImpl userReadService;

    @Override
    public List<UserAccountData> getAccountByUserId() {
        List<Account> accounts = entityManager.createQuery(
                "select main.account from UserAccount main \n" +
                        "where main.user.id = :userId and main.account.delDate is null", Account.class)
                .setParameter("userId", userReadService.getUser().getId())
                .getResultList();
        List<UserAccountData> accountData = new ArrayList<>();
        if (!accounts.isEmpty()) {
            accounts.forEach(data -> {
                accountData.add(
                        new UserAccountData(
                                data.getId(),
                                data.getName(),
                                data.getBalance(),
                                data.getChangeDate(),
                                data.getAddDate()
                        )
                );
            });
        }

        return accountData;
    }

    @Override
    public UserAccountData getAccountById(String id) {
        Account account = entityManager.find(Account.class, id);
        return new UserAccountData(
                account.getId(),
                account.getName(),
                account.getBalance(),
                account.getChangeDate(),
                account.getAddDate()
        );
    }
}
