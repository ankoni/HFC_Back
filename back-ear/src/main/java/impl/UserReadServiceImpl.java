package impl;
import api.UserReadService;
import model.user.UserDto;
import persistence.user.User;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
            List<User> user = getUserList();
            if (!user.isEmpty()) {
                return user.stream().map(it->new UserDto(it.getId(), it.getName(), it.getRegDate())).findFirst().get();
            }
        }
        return null;
    }

    public User getUserPersist() {
        if (ejbContext.getCallerPrincipal() != null) {
            return getUserList().get(0);
        }
        return null;
    }

    private List<User> getUserList() {
        return entityManager.createQuery("select main from User main where main.name =:name", User.class)
                .setParameter("name", ejbContext.getCallerPrincipal().getName()).getResultList();
    }
}
