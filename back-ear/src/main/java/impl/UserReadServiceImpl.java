package impl;
import api.UserReadService;
import model.auth.Permission;
import model.user.UserDto;
import persistence.privilege.Access;
import persistence.user.User;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Remote(UserReadService.class)
@LocalBean
@Stateless(name = "UserReadService")
public class UserReadServiceImpl implements UserReadService {
    @Resource
    EJBContext ejbContext;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public UserDto getUser() {
        if (ejbContext.getCallerPrincipal() != null) {
            User user = getUserPersist();
            if (user != null) {
                return new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getPrivilege().getName(),
                        user.getRegDate());
            }
        }
        return null;
    }

    public User getUserPersist() {
        if (ejbContext.getCallerPrincipal() != null) {
            return entityManager.find(User.class, ejbContext.getCallerPrincipal().getName());
        }
        return null;
    }

    @Override
    public boolean accessAllowForCurrentUser(Permission permission) {
        boolean allow = false;
        if (!ejbContext.getCallerPrincipal().getName().equals("anonymous")) {
            User user = getUserPersist();
            List<Access> accessList = user.getPrivilege().getAccessList().stream()
                    .filter(access -> access.getName().equals(permission)).collect(Collectors.toList());
            if (!accessList.isEmpty()) {
                allow = true;
            }
        }
        return allow;
    }
}
