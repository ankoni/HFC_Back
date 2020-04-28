package api.finance.record;

import model.finance.record.CreateFinanceRecordDto;
import model.finance.record.FinanceRecordTableRow;

import java.text.ParseException;
import java.util.List;

public interface FinanceRecordWriteService {
    FinanceRecordTableRow createUserFinanceRecord(CreateFinanceRecordDto data) throws ParseException;

    FinanceRecordTableRow editUserFinanceRecord(String id, FinanceRecordTableRow editData) throws ParseException;

    FinanceRecordTableRow deleteUserFinanceRecord(String id) throws ParseException;
}
