package api.account;

import model.account.UserAccountData;

import java.util.List;

public interface AccountWriteService {
    List<UserAccountData> updateUserAccounts(String id, List<UserAccountData> data);
}
