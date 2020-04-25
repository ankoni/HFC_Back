package model.account;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountData implements Serializable {
    public String id;
    public String name;
    public Double balance;
    /**
     * Дата изменения счета
     */
    public Date update;
    /**
     * Дата создания
     */
    public Date create;
    public boolean newRow;

    public UserAccountData(String id, String name, Double balance, Date update, Date create) {
        setId(id);
        setName(name);
        setBalance(balance);
        setUpdate(update);
        setCreate(create);
        setNewRow(false);
    }
}
