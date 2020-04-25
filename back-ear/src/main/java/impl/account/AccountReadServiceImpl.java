package impl.account;

import api.account.AccountReadService;
import impl.UserReadServiceImpl;
import model.account.AccountBalanceRow;
import model.account.UserAccountData;
import model.user.UserDto;
import persistence.EntityId;
import persistence.account.Account;
import persistence.account.UserAccount;
import persistence.account.balance.AccountBalance;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    @Override
    public Map<Date, List<AccountBalanceRow>> getUserDailyBalance() {
        List<UserAccount> accounts = entityManager.createQuery("select main from UserAccount main \n" +
                "where main.user.id = :user", UserAccount.class)
                .setParameter("user", userReadService.getUser().getId())
                .getResultList();
        List<AccountBalance> accountBalance = entityManager.createQuery("select main from AccountBalance main \n" +
                " where main.accountBalanceId.account.id in (:account) order by main.accountBalanceId.date desc", AccountBalance.class)
                .setParameter("account", accounts.stream().map(EntityId::getId).collect(Collectors.toList()))
                .getResultList();

        Map<Date, List<AccountBalanceRow>> dataTable = new TreeMap<>(Collections.reverseOrder());

        accountBalance.forEach(it -> {
            AccountBalanceRow row = new AccountBalanceRow(
                    it.getAccountBalanceId().getAccount().getName(),
                    it.getBalance()
            );

            if (dataTable.containsKey(it.getAccountBalanceId().getDate())) {
                dataTable.get(it.getAccountBalanceId().getDate()).add(row);
            } else {
                List<AccountBalanceRow> accountData = new ArrayList<>();
                accountData.add(row);
                dataTable.put(it.getAccountBalanceId().getDate(), accountData);
            }
        });

        return dataTable;
    }

    @Override
    public Double getUserTotalBalance() {
        UserDto user = userReadService.getUser();
        AtomicReference<Double> totalBalance = new AtomicReference<>(0.0);
        if (user != null) {
            List<Account> accounts = entityManager.createQuery("select main.account from UserAccount main \n" +
                    "where main.user.id = :userId", Account.class)
                    .setParameter("userId", user.getId())
                    .getResultList();
            accounts.forEach(account -> {
                totalBalance.updateAndGet(v -> v + account.getBalance());
            });
        }

        return totalBalance.get();
    }


}
