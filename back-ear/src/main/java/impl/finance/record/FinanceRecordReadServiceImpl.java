package impl.finance.record;

import api.finance.record.FinanceRecordReadService;
import impl.UserReadServiceImpl;
import model.IdNameObj;
import model.finance.record.FinanceRecordTableRow;
import persistence.finance.record.UserRecord;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Remote(FinanceRecordReadService.class)
@LocalBean
@Stateless(name = "FinanceRecordService")
public class FinanceRecordReadServiceImpl implements FinanceRecordReadService {

    @Inject
    UserReadServiceImpl userReadService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FinanceRecordTableRow> getUserRecords() {
        List<UserRecord> userRecords = entityManager.createQuery(
                "select main from UserRecord main where main.user.id = :user\n" +
                        " order by main.record.recordDate", UserRecord.class)
                .setParameter("user", userReadService.getUser().getId())
                .getResultList();

        List<FinanceRecordTableRow> records = new ArrayList<>();
        userRecords.forEach(record -> {
            records.add(new FinanceRecordTableRow(
                    record.getId(),
                    new IdNameObj(record.getRecord().getAccount().getId(), record.getRecord().getAccount().getName()),
                    record.getRecord().getAmount(),
                    new IdNameObj(record.getRecord().getCategory().getId(), record.getRecord().getCategory().getName()),
                    record.getRecord().getDescription(),
                    record.getRecord().getRecordDate(),
                    record.getRecord().getCreateDate()
            ));
        });
        return records;
    }
}
