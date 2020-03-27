package model.user;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserEditDto implements Serializable {
    public String name;
    public String currentPassword;
    public String newPassword;
}
