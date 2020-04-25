import api.auth.AuthService;
import com.sun.security.auth.UserPrincipal;
import model.auth.AuthData;
import model.auth.AuthError;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.naming.CommunicationException;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUpgradeHandler;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.security.Principal;

@Path("/auth")
@Produces({"application/json"})
@Consumes({"application/json"})
public class AuthRest {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @EJB(lookup = "java:jboss/exported/rest/AuthService!api.auth.AuthService")
    AuthService authService;

    @GET
    @Path("/check")
    public Principal checkAuth() {
        return request.getUserPrincipal();
    }

    @POST
    @Path("/login")
    public String authProcess(
            AuthData data
    ) throws ServletException, IOException {
        if (request.getUserPrincipal() == null) {
            try {
                request.login(data.getLogin(), data.getPassword());
                request.getSession().setAttribute("userName",
                        request.getUserPrincipal() != null
                                ? request.getUserPrincipal().getName() : null);
            } catch (ServletException ex) {
                return "Неверный логин или пароль";
            }
        } else {
            return "Пользователь уже в системе";
        }
        return null;
    }

    @POST
    @Path("/logout")
    public String authProcess() {
        try {
            request.logout();
            request.getSession().setAttribute("userName", null);
        } catch (ServletException e) {
            return e.getMessage();
        }
       return null;
    }
}
