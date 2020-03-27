package impl;

import api.UserReadService;
import api.UserWriteService;
import model.auth.AuthData;
import model.user.UserCreateDto;
import model.user.UserDto;
import model.user.UserEditDto;
import persistence.ConstData;
import persistence.privilege.Privilege;
import persistence.user.User;

import javax.annotation.Nullable;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.auth.login.LoginException;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Remote(UserWriteService.class)
@LocalBean
@Stateless(name = "UserWriteService")
public class UserWriteServiceImpl implements UserWriteService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public AuthData createUser(UserCreateDto dto) throws Exception {
        List<User> existUser = entityManager.createQuery("select main from User main \n" +
                "where main.name = :username", User.class)
                .setParameter("username", dto.getName())
                .getResultList();
        if (existUser.isEmpty()) {
            User user = new User(
                    UUID.randomUUID().toString(),
                    dto.getName(),
                    dto.getPassword(),
                    entityManager.find(Privilege.class, ConstData.PRIVILEGE_BASIC)
            );
            entityManager.persist(user);
            return new AuthData(
                    user.getName(),
                    user.getPassword()
            );
        } else {
            throw new Exception("Пользователь с таким именем уже зарегестрирован");
        }
    }

    @Override
    public UserDto editUser(String id, UserEditDto dto) throws Exception {
        User user = entityManager.find(User.class, id);

        if (dto.getCurrentPassword().equals(user.getPassword())) {
            if (isNotBlank(dto.getName())) {
                user.setName(dto.name);
            }
            if (isNotBlank(dto.getNewPassword())) {
                user.setPassword(dto.getNewPassword());
            }

            entityManager.flush();
        } else {
            throw new Exception("Пароль неверный");
        }

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getRegDate()
        );
    }

    @Nullable
    public boolean isNotBlank(String str) {
        if (str == null || str.equals("") || str.equals(" ")) {
            return false;
        }
        return true;
    }
}
