package model.user;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto implements Serializable {
    public String id;
    public String name;
    public String privilege;
    public Date regDate;
}
