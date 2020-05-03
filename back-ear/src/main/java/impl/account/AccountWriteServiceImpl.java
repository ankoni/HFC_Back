package impl.account;

import api.account.AccountWriteService;
import impl.UserReadServiceImpl;
import model.account.UserAccountData;
import persistence.account.Account;
import persistence.account.UserAccount;
import persistence.account.balance.AccountBalance;
import persistence.user.User;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.util.*;

@Remote(AccountWriteService.class)
@LocalBean
@Stateless(name = "AccountWriteService")
public class AccountWriteServiceImpl implements AccountWriteService {
    @PersistenceContext
    EntityManager entityManager;

    @Inject
    AccountReadServiceImpl accountReadService;

    @Inject
    UserReadServiceImpl userReadService;

    @Override
    public List<UserAccountData> updateUserAccounts(String id, List<UserAccountData> data) {
        User user = entityManager.find(User.class, id);
        List<UserAccount> userAccounts = entityManager.createQuery("select main from UserAccount main\n" +
                "where main.user.id = :user", UserAccount.class)
                .setParameter("user", id)
                .getResultList();
        if (!userAccounts.isEmpty()) {
            userAccounts.forEach(it -> {
                entityManager.remove(it);
                entityManager.remove(it.getAccount());
            });
            entityManager.flush();
        }

        if (!data.isEmpty()) {
            data.forEach(accountData -> {
                Account account = new Account(
                        accountData.getId() == null ? UUID.randomUUID().toString() : accountData.getId(),
                        accountData.getName(),
                        accountData.getBalance(),
                        accountData.getUpdate() == null ? new Date() : accountData.getUpdate(),
                        accountData.getCreate() == null ? new Date() : accountData.getCreate()
                );
                entityManager.persist(account);

                UserAccount userAccount = new UserAccount(
                        account.getId(),
                        user,
                        account
                );
                entityManager.persist(userAccount);

                if (accountData.getCreate() == null) {
                    accountData.setUpdate(new Date());
                    accountData.setCreate(new Date());
                }
                accountData.setNewRow(false);
            });
        }

        return data;
    }

    @Override
    public List<UserAccountData> createUserAccount(UserAccountData createDto) throws ParseException {
        Account newAccount = new Account(
                UUID.randomUUID().toString(),
                createDto.getName(),
                createDto.getBalance(),
                new Date(),
                new Date()
        );
        entityManager.persist(newAccount);

        UserAccount userAccount = new UserAccount(
                newAccount.getId(),
                userReadService.getUserPersist(),
                newAccount
        );
        entityManager.persist(userAccount);

        AccountBalance accountBalance = new AccountBalance();
        accountBalance = accountBalance.getAccountBalance(userAccount.getAccount(), new Date(), entityManager);

        if (accountBalance != null ) {
            accountBalance.setBalance(userAccount.getAccount().getBalance());
        } else {
            AccountBalance.createNewDailyBalance(entityManager, new Date(), userAccount.getUser().getId());
        }

        return accountReadService.getAccountByUserId();
    }

    @Override
    public List<UserAccountData> editUserAccount(UserAccountData editDto) {
        Account account = entityManager.find(Account.class, editDto.getId());
        account.setName(editDto.getName());
        return accountReadService.getAccountByUserId();
    }

    @Override
    public List<UserAccountData> deleteUserAccount(String deleteId) {
        entityManager.find(Account.class, deleteId).setDelDate(new Date());
        return accountReadService.getAccountByUserId();
    }


}
