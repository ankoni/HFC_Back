package api.finance.record;

import model.finance.record.CreateFinanceRecordDto;
import model.finance.record.FinanceRecordTableRow;

import java.text.ParseException;
import java.util.List;

public interface FinanceRecordWriteService {
    List<FinanceRecordTableRow> createUserFinanceRecord(CreateFinanceRecordDto data) throws ParseException;

    List<FinanceRecordTableRow> editUserFinanceRecord(String id, FinanceRecordTableRow editData) throws ParseException;

    List<FinanceRecordTableRow> deleteUserFinanceRecord(String id);
}
