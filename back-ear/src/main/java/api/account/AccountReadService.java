package api.account;

import model.account.UserAccountData;

import java.util.List;

public interface AccountReadService {
    List<UserAccountData> getAccountByUserId(String id);
}
