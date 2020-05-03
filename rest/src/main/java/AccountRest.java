import api.account.AccountReadService;
import api.account.AccountWriteService;
import model.account.AccountBalanceRow;
import model.account.UserAccountData;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Path("/account")
public class AccountRest {

    @EJB(lookup = "java:jboss/exported/back-ear/AccountReadService!api.account.AccountReadService")
    AccountReadService accountReadService;
    @EJB(lookup = "java:jboss/exported/back-ear/AccountWriteService!api.account.AccountWriteService")
    AccountWriteService accountWriteService;

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

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
    ) throws ParseException {
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

    @GET
    @Path("user/dailyBalance")
    public Map<Date, List<AccountBalanceRow>> getAccountBalanceTable() {
        return accountReadService.getUserDailyBalance();
    }

    @GET
    @Path("user/totalBalance")
    public Double getUserTotalBalance() {
        request.getUserPrincipal();
        return accountReadService.getUserTotalBalance();
    }
}
