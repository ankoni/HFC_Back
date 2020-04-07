package impl.finance.record;

import api.finance.record.FinanceRecordWriteService;
import impl.UserReadServiceImpl;
import model.IdNameObj;
import model.finance.record.CreateFinanceRecordDto;
import model.finance.record.FinanceRecordTableRow;
import persistence.ConstData;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    public List<FinanceRecordTableRow> createUserFinanceRecord(CreateFinanceRecordDto data) {
        User user = userReadService.getUserPersist();

        UserRecord userRecord = new UserRecord();
        userRecord.create(data, entityManager);
        userRecord.setUser(user);
        entityManager.persist(userRecord);

        Account account = userRecord.getRecord().getAccount();
        Category category = userRecord.getRecord().getCategory();

        if (data.changeAccountBalance) {
            account.changeBalance(category, data.getAmount(), false);

            List<AccountBalance> accountBalanceList = entityManager.createQuery("select main from AccountBalance main \n" +
                    "where main.accountBalanceId.account.id = :account and main.accountBalanceId.date = :date", AccountBalance.class)
                    .setParameter("account", account.getId())
                    .setParameter("date", data.recordDate)
                    .getResultList();
            if (!accountBalanceList.isEmpty()) {
                accountBalanceList.get(0).changeBalance(category, data.getAmount());
            } else {
                AccountBalanceId accountBalanceId = new AccountBalanceId(
                        account, data.getRecordDate()
                );
                AccountBalance accountBalance = new AccountBalance(
                        accountBalanceId,
                        account.getBalance()
                );
                entityManager.persist(accountBalance);
            }
        }

        return financeRecordReadService.getUserRecords();
    }

    @Override
    public List<FinanceRecordTableRow> editUserFinanceRecord(String id, FinanceRecordTableRow editData) {
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
                account.setBalance(account.getBalance().subtract(record.getAmount()));
            } else {
                account.setBalance(account.getBalance().add(record.getAmount()));
            }

            if (!accountBalanceList.isEmpty()) {
                AccountBalance accountBalance = accountBalanceList.get(0);
                if (record.getCategory().isIncome()) {
                    accountBalance.setBalance(accountBalance.getBalance().subtract(record.getAmount()));
                } else {
                    accountBalance.setBalance(accountBalance.getBalance().add(record.getAmount()));
                }
            }
        }

        entityManager.remove(entityManager.find(UserRecord.class, id));
        entityManager.remove(record);
        return financeRecordReadService.getUserRecords();
    }
}
