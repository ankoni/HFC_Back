package impl.finance.record;

import api.finance.record.FinanceRecordWriteService;
import impl.UserReadServiceImpl;
import model.finance.record.CreateFinanceRecordDto;
import model.finance.record.FinanceRecordTableRow;
import persistence.account.Account;
import persistence.account.balance.AccountBalance;
import persistence.account.balance.AccountBalanceId;
import persistence.category.Category;
import persistence.finance.record.Record;
import persistence.finance.record.UserRecord;
import persistence.user.User;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Remote(FinanceRecordWriteService.class)
@LocalBean
@Stateless(name = "FinanceRecordWriteService")
public class FinanceRecordWriteServiceImpl implements FinanceRecordWriteService {
    @Inject
    UserReadServiceImpl userReadService;

    @Inject
    FinanceRecordReadServiceImpl financeRecordReadService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FinanceRecordTableRow> createUserFinanceRecord(CreateFinanceRecordDto data) throws ParseException {
        User user = userReadService.getUserPersist();

        UserRecord userRecord = new UserRecord();
        userRecord.create(data, entityManager);
        userRecord.setUser(user);
        entityManager.persist(userRecord);

        Account account = userRecord.getRecord().getAccount();
        Category category = userRecord.getRecord().getCategory();

        if (data.changeAccountBalance) {
            account.changeBalance(category, data.getAmount(), false);

            AccountBalance accountBalance = new AccountBalance();
            accountBalance = accountBalance.getAccountBalance(account, data.getRecordDate(), entityManager);

            if (accountBalance != null ) {
                accountBalance.setBalance(account.getBalance());
//                accountBalance.changeBalance(category, data.getAmount());
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date createDate =  format.parse(format.format(data.getRecordDate()));

                List<Account> allAccounts = entityManager.createQuery("select main.account from UserAccount main \n" +
                        "where main.user.id = :userId", Account.class)
                        .setParameter("userId", user.getId())
                        .getResultList();
                allAccounts.forEach(it -> {
                    AccountBalance createAccountBalance = new AccountBalance(
                            new AccountBalanceId(it, createDate),
                            it.getBalance()
                    );
                    entityManager.persist(createAccountBalance);
                });

            }
        }

        return financeRecordReadService.getUserRecords();
    }

    @Override
    public List<FinanceRecordTableRow> editUserFinanceRecord(String id, FinanceRecordTableRow editData) throws ParseException {
        Record record = entityManager.find(Record.class, id);

        record.editRecord(editData, entityManager);

        return financeRecordReadService.getUserRecords();
    }

    @Override
    public List<FinanceRecordTableRow> deleteUserFinanceRecord(String id) {
        Record record = entityManager.find(Record.class, id);
        Account account = record.getAccount();
        if (record.accountInclude) {
            List<AccountBalance> accountBalanceList = entityManager.createQuery("select main from AccountBalance main \n" +
                    "where main.accountBalanceId.account.id = :account and main.accountBalanceId.date = :date", AccountBalance.class)
                    .setParameter("account", account.getId())
                    .setParameter("date", record.getRecordDate())
                    .getResultList();
            if (record.getCategory().isIncome()) {
                account.setBalance(account.getBalance() - record.getAmount());
            } else {
                account.setBalance(account.getBalance() + record.getAmount());
            }

            if (!accountBalanceList.isEmpty()) {
                AccountBalance accountBalance = accountBalanceList.get(0);
                if (record.getCategory().isIncome()) {
                    accountBalance.setBalance( accountBalance.getBalance() - record.getAmount());
                } else {
                    accountBalance.setBalance(accountBalance.getBalance() + record.getAmount());
                }
            }
        }

        entityManager.remove(entityManager.find(UserRecord.class, id));
        entityManager.remove(record);
        return financeRecordReadService.getUserRecords();
    }
}
