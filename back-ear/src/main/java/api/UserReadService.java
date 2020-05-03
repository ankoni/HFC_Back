package api;

import model.auth.Permission;
import model.user.UserDto;
import persistence.user.User;

public interface UserReadService {
    UserDto getUser();
    User getUserPersist();

    boolean accessAllowForCurrentUser(Permission permission);
}
