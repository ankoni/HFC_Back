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
    @Path("user/{id}")
    public List<UserAccountData> getUserAccounts(
            @PathParam("id") String id
    ) {
        return accountReadService.getAccountByUserId(id);
    }

    @PUT
    @Path("user/{id}/update")
    public List<UserAccountData> updateUserAccounts(
            @PathParam("id") String id,
            List<UserAccountData> data
    ) {
        return accountWriteService.updateUserAccounts(id, data);
    }
}
