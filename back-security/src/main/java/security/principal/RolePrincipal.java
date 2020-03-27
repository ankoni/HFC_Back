package security.principal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Principal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolePrincipal implements Principal {
    private String name;
}
