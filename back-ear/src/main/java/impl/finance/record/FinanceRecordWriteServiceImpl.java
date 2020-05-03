package impl.finance.record;

import api.finance.record.FinanceRecordWriteService;
import impl.UserReadServiceImpl;
import model.finance.record.CreateFinanceRecordDto;
import model.finance.record.FinanceRecordTableRow;
import persistence.account.Account;
import persistence.account.balance.AccountBalance;
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

@Remote(FinanceRecordWriteService.class)
@LocalBean
@Stateless(name = "FinanceRecordWriteService")
public class FinanceRecordWriteServiceImpl implements FinanceRecordWriteService {
    @Inject
    UserReadServiceImpl userReadService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public FinanceRecordTableRow createUserFinanceRecord(CreateFinanceRecordDto data) throws ParseException {
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
            } else {
                AccountBalance.createNewDailyBalance(entityManager, data.getRecordDate(), user.getId());
            }
        }

        return userRecord.getRecord().convertToTableRow();
    }

    @Override
    public FinanceRecordTableRow editUserFinanceRecord(String id, FinanceRecordTableRow editData) throws ParseException {
        Record record = entityManager.find(Record.class, id);

        record.editRecord(editData, entityManager);

        return editData;
    }

    @Override
    public FinanceRecordTableRow deleteUserFinanceRecord(String id) throws ParseException {
        Record record = entityManager.find(Record.class, id);
        Account account = record.getAccount();
        if (record.accountInclude) {
            AccountBalance accountBalance = new AccountBalance();
            accountBalance = accountBalance.getAccountBalance(account, record.getRecordDate(), entityManager);
            account.changeBalance(record.getCategory(), record.getAmount(), true);
            accountBalance.setBalance(account.getBalance());
        }

        entityManager.remove(entityManager.find(UserRecord.class, id));
        entityManager.remove(record);
        return record.convertToTableRow();
    }
}
