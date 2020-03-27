package model.auth;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthData implements Serializable {
    String login;
    String password;
}
