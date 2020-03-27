package impl.account;

import api.account.AccountReadService;
import api.account.AccountWriteService;
import model.account.UserAccountData;
import persistence.account.Account;
import persistence.account.UserAccount;
import persistence.user.User;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;
import java.util.stream.Collectors;

@Remote(AccountWriteService.class)
@LocalBean
@Stateless(name = "AccountWriteService")
public class AccountWriteServiceImpl implements AccountWriteService {
    @PersistenceContext
    EntityManager entityManager;

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
}
