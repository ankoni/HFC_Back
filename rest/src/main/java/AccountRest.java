import api.account.AccountReadService;
import api.account.AccountWriteService;
import model.account.UserAccountData;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.util.List;

@Path("/account")
public class AccountRest {

    @EJB(lookup = "java:jboss/exported/back-ear/AccountReadService!api.account.AccountReadService")
    AccountReadService accountReadService;
    @EJB(lookup = "java:jboss/exported/back-ear/AccountWriteService!api.account.AccountWriteService")
    AccountWriteService accountWriteService;

    @GET
    @Path("user")
    public List<UserAccountData> getUserAccounts() {
        return accountReadService.getAccountByUserId();
    }

    @PUT
    @Path("user/{id}/update")
    public List<UserAccountData> updateUserAccounts(
            @PathParam("id") String id,
            List<UserAccountData> data
    ) {
        return accountWriteService.updateUserAccounts(id, data);
    }

    @POST
    @Path("user/create")
    public List<UserAccountData> createUserAccount(
            UserAccountData data
    ) {
        return accountWriteService.createUserAccount(data);
    }

    @POST
    @Path("user/edit")
    public List<UserAccountData> editUserAccount(
            UserAccountData data
    ) {
        return accountWriteService.editUserAccount(data);
    }

    @POST
    @Path("user/delete/{id}")
    public List<UserAccountData> deleteUserAccount(
            @PathParam("id") String id
    ) {
        return accountWriteService.deleteUserAccount(id);
    }

    @GET
    @Path("{id}")
    public UserAccountData getAccountById(
            @PathParam("id") String id
    ) {
        return accountReadService.getAccountById(id);
    }
}
