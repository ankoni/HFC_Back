package model.user;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserCreateDto implements Serializable {
    public String name;
    public String password;
}
