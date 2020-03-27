package api;

import model.auth.AuthData;
import model.user.UserCreateDto;
import model.user.UserDto;
import model.user.UserEditDto;

import javax.security.auth.login.LoginException;

public interface UserWriteService {
    AuthData createUser(UserCreateDto dto) throws Exception;

    UserDto editUser(String id, UserEditDto dto) throws Exception;
}
