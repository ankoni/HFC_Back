package impl.auth;

import api.auth.AuthService;
import com.sun.security.auth.UserPrincipal;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.*;
import java.security.Principal;

@Remote(AuthService.class)
@LocalBean
@Stateless(name = "AuthService")
public class AuthServiceImpl implements AuthService {
    @Resource
    EJBContext ejbContext;

    public Principal getCurrentUser() {
        return getUserPrincipal();
    }

    public Principal getUserPrincipal() {
        return ejbContext.getCallerPrincipal();
    }

}
