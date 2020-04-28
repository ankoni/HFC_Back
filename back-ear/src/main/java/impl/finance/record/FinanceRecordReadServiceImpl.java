package impl.finance.record;

import api.finance.record.FinanceRecordReadService;
import impl.UserReadServiceImpl;
import model.FilterData;
import model.IdNameObj;
import model.filter.ValueType;
import model.finance.record.FinanceRecordTableRow;
import org.hibernate.ejb.criteria.predicate.ComparisonPredicate;
import org.hibernate.type.EntityType;
import persistence.finance.record.Record;
import persistence.finance.record.UserRecord;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
    public List<FinanceRecordTableRow> getUserRecords(List<FilterData> filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserRecord> query = builder.createQuery(UserRecord.class);
        Root<UserRecord> main = query.from(UserRecord.class);

        List<Predicate> whereList = new LinkedList<Predicate>();
        if (!filter.isEmpty()) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            filter.forEach(column -> {
                Expression<?> expression = null;
                Object val = null;
                if (column.value.id != null) {
                    expression = main.get("record").get(column.columnName).get("id");
                    val = column.value.id;
                } else {
                    expression = main.get("record").get(column.columnName);
                    val = column.value.name;
                }
                switch (column.conditionType) {
                    case TO:
                        if (column.valueType == ValueType.DATE ) {
                            try {
                                whereList.add(builder.lessThanOrEqualTo((Expression<? extends Date>) expression, format.parse(val.toString())));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            whereList.add(builder.lessThanOrEqualTo((Expression<? extends Double>) expression, Double.parseDouble(val.toString())));
                        }
                        break;
                    case FROM:
                        if (column.valueType == ValueType.DATE ) {
                            try {
                                whereList.add(builder.greaterThanOrEqualTo((Expression<? extends Date>) expression, format.parse(val.toString())));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            whereList.add(builder.greaterThanOrEqualTo((Expression<? extends Double>) expression, Double.parseDouble(val.toString())));
                        }
                        break;
                    case LIKE:
                        whereList.add(builder.like((Expression<String>) expression, (String) val));
                        break;
                    case EQUAL:
                    default:
                        whereList.add(builder.equal(expression, val));
                        break;
                }
            });
        }
        whereList.add(builder.equal(main.get("user").get("id"), userReadService.getUser().getId()));
        Predicate[] where = new Predicate[whereList.size()];
        whereList.toArray(where);
        query = query.select(main).distinct(true).where(where);
        List<UserRecord> userRecords = entityManager.createQuery(query).getResultList();

        List<FinanceRecordTableRow> records = new ArrayList<>();
        userRecords.forEach(record -> {
            records.add(record.getRecord().convertToTableRow());
        });
        return records;
    }

    @Override
    public FinanceRecordTableRow getUserRecordById(String id) {
        Record record = entityManager.find(Record.class, id);
        return record.convertToTableRow();
    }
}
