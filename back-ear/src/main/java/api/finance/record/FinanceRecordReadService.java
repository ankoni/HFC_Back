package api.finance.record;

import model.FilterData;
import model.finance.record.FinanceRecordTableRow;

import java.util.List;

public interface FinanceRecordReadService {
    List<FinanceRecordTableRow> getUserRecords(List<FilterData> filter);

    FinanceRecordTableRow getUserRecordById(String id);
}
