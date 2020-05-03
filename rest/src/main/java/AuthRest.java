import api.UserReadService;
import model.auth.AuthData;
import model.auth.Permission;
import model.user.UserDto;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/auth")
@Produces({"application/json"})
@Consumes({"application/json"})
public class AuthRest {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @EJB(lookup = "java:jboss/exported/rest/UserReadService!api.UserReadService")
    UserReadService userReadService;

    @GET
    @Path("/check")
    public UserDto checkAuth() {
        return userReadService.getUser();
    }

    @POST
    @Path("/login")
    public Response authProcess(
            AuthData data,
            @Context HttpServletRequest httpRequest
    ) throws ServletException, IOException {
        if (httpRequest.getUserPrincipal() == null) {
            try {
                request.login(data.getLogin(), data.getPassword());
                Response res = Response.accepted().build();
                return res;
            } catch (ServletException ex) {
                throw new ServletException(ex.getMessage());
            }
        } else {
            throw new ServletException("Пользователь уже в системе");
        }
    }

    @POST
    @Path("/logout")
    public String authProcess() {
        try {
            request.logout();
        } catch (ServletException e) {
            return e.getMessage();
        }
       return null;
    }

    @POST
    @Path("checkPermission")
    @Consumes("text/plain")
    public boolean checkPermission(
            Permission permission
    ) {
        return userReadService.accessAllowForCurrentUser(permission);
    }
}
