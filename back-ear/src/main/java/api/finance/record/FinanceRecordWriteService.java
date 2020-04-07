package api.finance.record;

import model.finance.record.CreateFinanceRecordDto;
import model.finance.record.FinanceRecordTableRow;

import java.util.List;

public interface FinanceRecordWriteService {
    List<FinanceRecordTableRow> createUserFinanceRecord(CreateFinanceRecordDto data);

    List<FinanceRecordTableRow> editUserFinanceRecord(String id, FinanceRecordTableRow editData);

    List<FinanceRecordTableRow> deleteUserFinanceRecord(String id);
}
