package security.auth;

import org.jboss.annotation.security.SecurityDomain;
import security.principal.RolePrincipal;
import security.principal.UserPrincipal;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
@SecurityDomain("SURDAuth")
public class BackLoginModule implements LoginModule {
    private CallbackHandler handler;
    private Subject subject;
    private Map<String, ?> options;
    private String login;
    private List<String> privilege;
    private RolePrincipal rolePrincipal;
    private UserPrincipal userPrincipal;

    private String GET_USER_SQL = "select id from user_data where name=? and password=?";
    private String GET_PRIVILEGE_SQL = "select role.name as name from user_data u join PRIVILEGE role on role.id=u.PRIVILEGE_ID where u.name=?";

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        handler = callbackHandler;
        this.subject = subject;
        this.options = options;
    }

    @Override
    public boolean login() throws LoginException {
        DataSource dataSource;
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("login");
        callbacks[1] = new PasswordCallback("password", true);
        try {
            handler.handle(callbacks);
            String name = ((NameCallback) callbacks[0]).getName();
            String password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());

            String dataSourceJndi = (String) options.get("dsJndiName");
            if (dataSourceJndi == null) {
                throw new SQLException("Not connected");
            }
            dataSource = (DataSource) new InitialContext().lookup(dataSourceJndi);

            PreparedStatement connection = dataSource.getConnection().prepareStatement(GET_USER_SQL);

            connection.setString(1, name);
            connection.setString(2, password);

            ResultSet resultSet = connection.executeQuery();
            String id = null;
            while (resultSet.next()) {
                id = resultSet.getString("id");
            }

            boolean userExist = id != null;
            if (name != null && userExist) {
                login = name;

                connection = dataSource.getConnection().prepareStatement(GET_PRIVILEGE_SQL);
                connection.setString(1, name);
                resultSet = connection.executeQuery();
                this.privilege = new ArrayList<String>();
                while (resultSet.next()) {
                    this.privilege.add(resultSet.getString("name"));
                }
                dataSource.getConnection().close();
                return true;
            } else {
                throw new LoginException("Неверный логин или пароль");
            }
        } catch (IOException | UnsupportedCallbackException | NamingException | SQLException e) {
            throw new LoginException(e.getMessage());
        }
    }

    @Override
    public boolean commit() throws LoginException {
        userPrincipal = new UserPrincipal(login);
        subject.getPrincipals().add(userPrincipal);
        if (privilege != null && privilege.size() > 0) {
            for (String groupName : privilege) {
                rolePrincipal = new RolePrincipal(groupName);
                subject.getPrincipals().add(rolePrincipal);
            }
        }
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return false;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().remove(userPrincipal);
        subject.getPrincipals().remove(rolePrincipal);
        return false;
    }
}
