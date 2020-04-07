package api.account;

import model.account.UserAccountData;

import java.util.List;

public interface AccountWriteService {
    List<UserAccountData> updateUserAccounts(String id, List<UserAccountData> data);

    List<UserAccountData> createUserAccount(UserAccountData createDto);

    List<UserAccountData> editUserAccount(UserAccountData editDto);

    List<UserAccountData> deleteUserAccount(String deleteId);
}
