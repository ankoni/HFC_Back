package api.auth;

import com.sun.security.auth.UserPrincipal;
import model.auth.AuthData;

import java.security.*;

public interface AuthService {
    Principal getCurrentUser();

}
