package api;

import model.user.UserDto;
import persistence.user.User;

import java.util.List;

public interface UserReadService {
    UserDto getUser();
}
