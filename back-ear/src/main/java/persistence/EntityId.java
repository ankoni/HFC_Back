package persistence;

import com.sun.corba.se.spi.ior.Identifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class EntityId implements Serializable {
    @Id
    protected String id;

    public EntityId(String id) {
        setId(id);
    }
}
