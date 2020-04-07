package api.finance.record;

import model.finance.record.FinanceRecordTableRow;

import java.util.List;

public interface FinanceRecordReadService {
    List<FinanceRecordTableRow> getUserRecords();
}
