package api.account;

import model.account.AccountBalanceRow;
import model.account.UserAccountData;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AccountReadService {
    List<UserAccountData> getAccountByUserId();

    UserAccountData getAccountById(String id);

    Map<Date, List<AccountBalanceRow>> getUserDailyBalance();

    Double getUserTotalBalance();
}
